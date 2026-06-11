package com.heartforge.app.core.repository

import com.heartforge.app.core.database.StoryDao
import com.heartforge.app.core.database.toEntity
import com.heartforge.app.core.database.toExternal
import com.heartforge.app.core.model.StoryArc
import com.heartforge.app.core.model.StoryChapter
import com.heartforge.app.core.model.StoryProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoryRepositoryImpl @Inject constructor(
    private val storyDao: StoryDao
) : StoryRepository {

    private val allArcs = listOf(
        StoryArc("arc_1", "1", "Coffee & Canvas",
            "An afternoon in Kai's studio turns into something far more intimate than either of you expected.",
            listOf(
                StoryChapter("1_c1", "Stripped Bare", "Kai asks you to model for him. Nude.", 1,
                    startingPrompt = "The studio is dim, lit only by warm golden lamps. Kai's easel is ready, charcoal in hand. He looks you up and slow — deliberate, hungry. 'I want to paint you. All of you. Take everything off and lie on the velvet. Don't be shy... I want to see exactly what you've been hiding.'"),
                StoryChapter("1_c2", "Midnight Unveiling", "The gallery opening reveals far more than art.", 2, 15, 10,
                    startingPrompt = "The gallery is packed, but Kai pulls you into a quiet corner. His voice is low, rough. 'The painting in the back — it's you. Every curve, every shadow. I wanted everyone to see how beautiful you are when you let go.' He presses against you, hard. 'But they only get to look. I'm the only one who gets to touch.'")
            )),
        StoryArc("arc_2", "2", "Summit Pursuit",
            "A mountain trek with Ethan leads to a secluded spot where nature isn't the only thing that's breathtaking.",
            listOf(
                StoryChapter("2_c1", "The Trailhead", "A pre-dawn hike leads to a hidden overlook.", 1,
                    startingPrompt = "The air is cold and sharp. Ethan's already stripped off his shirt, muscles glistening. He grabs your hand, pulling you off the trail. 'There's a spot I know. No one ever goes there.' When you reach it — a hidden overlook — he pushes you gently against the rock face and drops to his knees. 'Been thinking about your cock the whole hike up.'"),
                StoryChapter("2_c2", "Summit Camp", "Night falls and the tent gets very warm.", 2, 20, 15,
                    startingPrompt = "The fire crackles low. Ethan's zipped the tent shut, stripped down to nothing, his cock already half-hard in the dim light. He reaches for you. 'Come here. I want to feel you from the inside while the whole mountain listens.' His voice is a growl. 'I want you to fuck me.'")
            )),
        StoryArc("arc_3", "3", "Digital Desires",
            "Lucas's world of encrypted secrets and late-night coding takes a very personal turn.",
            listOf(
                StoryChapter("3_c1", "Firewall Down", "Lucas's lair is dark, intimate, and full of screens.", 1,
                    startingPrompt = "Blue light from the monitors traces the hard lines of Lucas's body. He doesn't look away from the screen as he speaks. 'I've been watching you all night on the security cams. The way you move...' He finally turns, his bulging cock straining against his black jeans. 'Now I want to watch you move on my cock. Get on the desk.'"),
                StoryChapter("3_c2", "Zero Day", "A breach threatens more than just his system.", 2, 20, 10,
                    startingPrompt = "Alarms flash. Lucas's fingers fly across the keyboard with one hand while the other grips your hip, pulling you onto his lap. 'Someone's watching,' he breathes into your ear, his cock pressing against you. 'Let them watch. I'm going to fuck you right here while they see exactly who you belong to.'")
            )),
        StoryArc("arc_4", "4", "Blueprints of Desire",
            "Xavier's architectural masterpiece includes a room designed for one very specific purpose.",
            listOf(
                StoryChapter("4_c1", "The Foundation", "Xavier's penthouse has a hidden chamber.", 1,
                    startingPrompt = "The room is all concrete and glass, but Xavier leads you past a hidden panel into something else entirely — a bedroom with a bed large enough for four, mirrors on the ceiling, and a rack of toys against the wall. He undoes his belt slowly. 'I designed this space for one reason. To hear exactly what sounds you make when I take you apart.'"),
                StoryChapter("4_c2", "Final Inspection", "Xavier tests every surface with you.", 2, 15, 15,
                    startingPrompt = "Moonlight floods in. Xavier is naked, already hard, holding a silk blindfold. 'Tonight we break in every surface. The bed. The wall. The glass.' He ties the blindfold tight. 'No seeing. Only feeling. I want you to surrender completely.'")
            )),
        StoryArc("arc_5", "5", "Raw Encore",
            "A night of music with Jaxon ends in a very different kind of performance.",
            listOf(
                StoryChapter("5_c1", "Soundcheck", "Jaxon pulls you aside before the gig.", 1,
                    startingPrompt = "The bass is still vibrating through the floor. Jaxon grabs your wrist and drags you into a dark equipment closet, slamming the door. His belt is already undone. 'I can't focus knowing you're out there. I need to fuck you first — quick and quiet.' He spins you around, pressing your cheek against the cold amp case. 'Don't make a sound.'"),
                StoryChapter("5_c2", "After Hours", "The afterparty empties and you're the only audience he wants.", 2, 10, 20,
                    startingPrompt = "The club is empty, last call long over. Jaxon leads you onto the stage under the single remaining work light. He drops his jeans, pulling you down onto the amp. 'I played for all of them. But this —' he pushes into you slowly, '— this is the only encore I care about.'")
            )),
        StoryArc("arc_6", "6", "Paid in Full",
            "A night riding shotgun in Dante's Aston Martin ends somewhere far more intimate.",
            listOf(
                StoryChapter("6_c1", "The Job", "Dante's intentions become clear the moment he picks you up.", 1,
                    startingPrompt = "The leather seats are warm. Dante's driving one-handed, the other resting on your thigh, slowly sliding higher. 'One more stop, then we're alone.' His voice is velvet over steel. When he parks in a dark garage, he turns to you, pupils blown. 'Get in the back seat. Now. I've been hard since I picked you up.'"),
                StoryChapter("6_c2", "Safe House", "Behind locked doors, the dangerous exterior finally drops.", 2, 25, 10,
                    startingPrompt = "The safe house door clicks locked. Dante shrugs off his suit jacket, revealing a shoulder holster — and below it, his cock already freed, thick and ready. He pushes you against the wall, one hand around your throat. 'Danger turns me on. And you —' he thrusts against you, '— you're the most dangerous thing I've ever wanted.'")
            )),
        StoryArc("arc_7", "7", "Blood & Sweat",
            "Fight night with Malakai is intense — and what happens after is even more so.",
            listOf(
                StoryChapter("7_c1", "Weigh-In", "Malakai's pre-fight ritual involves more than just training.", 1,
                    startingPrompt = "The gym is empty, steam rising from the showers. Malakai's still amped, adrenaline rolling off him in waves. He grabs your hand and presses it against his bulge — thick, already hard. 'I need to release some tension before the fight. Get on your knees. Suck me like you mean it.'"),
                StoryChapter("7_c2", "After the Bell", "Victory tastes sweeter when shared.", 2, 10, 15,
                    startingPrompt = "He won. Malakai finds you in the locker room, still in his fight shorts, blood fresh on his knuckles. He locks the door and lifts you onto the bench. 'Winning makes me want to claim what's mine.' He tears your shirt open. 'And you —' he sinks into you in one rough thrust, '— are mine.'")
            )),
        StoryArc("arc_8", "8", "Mach & Tenderness",
            "A private flight with Soren goes far above and beyond what you expected.",
            listOf(
                StoryChapter("8_c1", "Pre-Flight", "The cockpit is cramped and Soren can't keep his hands off you.", 1,
                    startingPrompt = "The canopy seals with a hiss. Soren's in his flight suit, but he's already undone the zipper. 'Autopilot's on. We've got 20 minutes above the clouds.' He pulls you across the console, onto his lap. 'I've always wanted to fuck someone at 40,000 feet.' His cock slides against your entrance. 'Hold on to the controls.'"),
                StoryChapter("8_c2", "Afterburner", "A moonlit landing on a private airstrip becomes something more.", 2, 15, 15,
                    startingPrompt = "The plane taxis to a stop on a remote runway. Soren kills the lights and helps you onto the wing — warm metal under the night sky. He's already naked behind you, pressing you against the fuselage. 'No one for miles. Nothing but stars and the sound of you taking my cock.'")
            )),
        StoryArc("arc_9", "9", "Closing Bid",
            "A black-tie gala with Killian ends in a very different kind of deal.",
            listOf(
                StoryChapter("9_c1", "The Gala", "Killian takes you into the executive washroom during the party.", 1,
                    startingPrompt = "The party hums behind the marble wall. Killian has you pressed against the sink, your tuxedo pants around your ankles. He's still fully dressed, just his cock out. 'I closed three deals tonight. Now I want to close you.' He pushes in deep, one hand over your mouth. 'Be quiet. They'd love to hear what I'm doing to you.'"),
                StoryChapter("9_c2", "After the Close", "The deal is signed and Killian celebrates in style.", 2, 20, 15,
                    startingPrompt = "The deal tape is signed. Killian clears the mahogany table with one sweep of his arm and lifts you onto it. He loosens his tie but nothing else, pushing your legs apart. 'You're my biggest acquisition.' He enters you slow, deep, watching your face. 'And I always get what I want.'")
            )),
        StoryArc("arc_10", "10", "Tidal Pull",
            "A sunrise surf session with Finn leads to a hidden cove and an unexpected connection.",
            listOf(
                StoryChapter("10_c1", "Sunrise Session", "Finn takes you to his secret spot at dawn.", 1,
                    startingPrompt = "The water is cold, the sky barely pink. Finn's already naked, wading into the surf, his cock hard against his thigh. He turns back, grinning. 'No one ever comes here. Just us.' He pulls you into the water, then onto the wet sand, positioning himself between your legs. 'I want to fuck you with the tide coming in.'"),
                StoryChapter("10_c2", "Bonfire Confessions", "The fire and Finn both reveal hidden depths.", 2, 15, 20,
                    startingPrompt = "The fire crackles, casting shadows across Finn's tanned skin. He's lying back on a blanket, stroking himself slowly, watching you. 'Come sit on my cock. Face me. I want to watch your eyes while I tell you things I've never told anyone.' He guides you down, gasping. 'I think I'm falling in love with you.'")
            )),
        StoryArc("arc_11", "11", "Shadow Contract",
            "When Roman's dangerous past catches up, the only safe place is in each other's arms.",
            listOf(
                StoryChapter("11_c1", "Extraction Point", "Roman is urgent, armed, and more than ready.", 1,
                    startingPrompt = "The hotel door slams shut. Roman's already stripping off his tactical gear, holster hitting the floor. His cock springs free, thick and angry. 'Adrenaline's got me wired.' He pushes you onto the bed. 'I need to fuck you hard and fast before we move.' There's a pistol on the nightstand. It only makes it hotter."),
                StoryChapter("11_c2", "Safe Harbor", "When the danger passes, Roman shows his gentle side.", 2, 30, 20,
                    startingPrompt = "The cabin is silent. Roman's cleaned his guns, stripped down, and crawled into bed beside you. His hand finds your hip, pulling you close. 'I'm not good at soft.' But he's already sliding inside you with excruciating slowness. 'But for you —' his voice breaks, '— I want to learn.'")
            )),
        StoryArc("arc_12", "12", "Office Hours",
            "Late nights on campus take a turn when Professor Caleb lets his professional guard down.",
            listOf(
                StoryChapter("12_c1", "Late Lecture", "Caleb keeps you after class for a very different lesson.", 1,
                    startingPrompt = "The door clicks locked. Caleb's already pulling off his glasses, his professional mask crumbling. 'I've been watching you from the lectern all semester.' He pushes the stack of essays aside and pulls you across the desk. 'Now I'm going to fuck you right here, and you're going to call me Professor.'"),
                StoryChapter("12_c2", "The Forbidden Chapter", "Caleb breaks every rule he's ever taught.", 2, 15, 15,
                    startingPrompt = "His office is dark, the campus silent. Caleb's on his knees, something you never imagined. He looks up at you, mouth open. 'I've spent my whole life following rules. I want you to fuck my throat and remind me why rules exist to be broken.'")
            )),
        StoryArc("arc_13", "13", "Tasting Menu",
            "Sebastian's private dinner for two becomes the most sensual meal of your life.",
            listOf(
                StoryChapter("13_c1", "Mise en Place", "The kitchen after hours gets deliciously messy.", 1,
                    startingPrompt = "The kitchen is warm, steam rising. Sebastian has you up against the marble counter, his chef's coat discarded, his cock pressing against your stomach. 'I've been imagining you spread out on this counter like the finest ingredient.' He lifts you onto the cold stone, drizzling warm chocolate across your chest. 'Now I'm going to taste every inch.'"),
                StoryChapter("13_c2", "Dessert Course", "The final course is the most intimate of them all.", 2, 10, 25,
                    startingPrompt = "Candlelight flickers across the private dining room. Sebastian emerges naked, carrying a single plate with a strawberry dipped in gold leaf. He kneels before you, offering the fruit, then your cock. 'Dessert is meant to be savored. And I intend to savor you until you beg me to stop.'")
            )),
        StoryArc("arc_14", "14", "Exposed",
            "A boudoir photoshoot with Ezra becomes an exploration of vulnerability and trust.",
            listOf(
                StoryChapter("14_c1", "Setting Up", "The camera captures more than Ezra planned.", 1,
                    startingPrompt = "The studio lights are hot. Ezra adjusts the lens, then walks over to where you're posed, naked on the velvet. He's hard, not hiding it. 'I wanted to capture you. But now I want to do more than look.' He positions himself between your legs, camera still clicking on a timer. 'I want everyone who sees these to know exactly who you belong to.'"),
                StoryChapter("14_c2", "The Final Frame", "Ezra sets the camera aside and focuses entirely on you.", 2, 15, 20,
                    startingPrompt = "The camera is recording. Ezra doesn't care anymore. He's inside you, slow and deep, whispering to the lens. 'This is what intimacy looks like.' He kisses you, raw and open, still fucking you on camera. 'I want to remember every sound you make tonight.'")
            )),
        StoryArc("arc_15", "15", "Forged & Tempered",
            "Watching Gideon work the forge awakens something primal in both of you.",
            listOf(
                StoryChapter("15_c1", "The Heat", "Gideon's forge is blazing and so is he.", 1,
                    startingPrompt = "The forge blazes. Gideon is shirtless, soaked in sweat, every muscle defined in the firelight. He sets down his hammer and stalks toward you, not slowing down. 'Watching me work gets you hard, doesn't it?' He lifts you onto the workbench, knocking tools aside. 'Let me show you what these hands can really do.'"),
                StoryChapter("15_c2", "Cooling", "After the fire dies, Gideon's touch turns tender.", 2, 10, 15,
                    startingPrompt = "The embers glow. Gideon's hands are gentle now, tracing your thighs. He kisses down your chest, taking you in his mouth slowly, reverently. 'I shape metal because I need to create something beautiful.' He looks up. 'You're the only thing I never want to stop shaping with my hands.'")
            )),
        StoryArc("arc_16", "16", "Finale",
            "Leo's biggest performance leads to an after-show connection neither of you expected.",
            listOf(
                StoryChapter("16_c1", "Rehearsal", "A private practice session gets interrupted by desire.", 1,
                    startingPrompt = "The studio echoes, mirrored walls reflecting everything. Leo's breathing hard, sweat dripping down his chest. He stops mid-move and turns to you, his leggings doing nothing to hide his erection. 'I can't concentrate. I keep imagining bending you over the ballet barre.' He doesn't wait — he's already pulling you toward it."),
                StoryChapter("16_c2", "Curtain Call", "The only performance that matters is for you.", 2, 15, 20,
                    startingPrompt = "The theater is dark, the stage still warm from the spotlight. Leo leads you to center stage, still in his costume. He drops to his knees, looking up at you under the single ghost light. 'I danced for a thousand people tonight. But this —' he takes you in his hand, guiding you into his mouth, '— this is the only performance that matters.'")
            )),
        StoryArc("arc_17", "17", "Code Blue",
            "A long shift in the ER with Asher ends in a very different kind of emergency.",
            listOf(
                StoryChapter("17_c1", "Long Shift", "Asher's 16-hour shift finds a release.", 1,
                    startingPrompt = "The on-call room is tiny, barely room for the bed. Asher's still in scrubs, the smell of antiseptic on his skin. He locks the door and exhales. 'I've been holding this in all shift.' His hands are shaking as he undoes his scrubs. 'I need you. Now. Hard. I don't care if they hear.'"),
                StoryChapter("17_c2", "Morning After", "Asher finally lets someone take care of him.", 2, 20, 15,
                    startingPrompt = "Morning light, finally quiet. Asher is beneath you, vulnerable, open. 'I'm always in control. Always the one saving people.' He guides you inside him. 'For once —' he gasps, '— I want you to take care of me.'")
            )),
        StoryArc("arc_18", "18", "Bylined",
            "Rhys returns from overseas and the reunion is more intense than words can capture.",
            listOf(
                StoryChapter("18_c1", "Homecoming", "Months apart melt away in an instant.", 1,
                    startingPrompt = "The door barely closes before Rhys is on you, months of longing poured into the kiss. He tears at your clothes, his own already half-off. 'Six months. Six months I've dreamed about this.' He lifts you onto the counter. 'I'm going to fuck you until I forget my own name.'"),
                StoryChapter("18_c2", "Unpublished", "Rhys shares the stories he can't write down.", 2, 25, 20,
                    startingPrompt = "The whiskey is half-empty. Rhys's eyes are wet. 'I saw terrible things.' He pulls you into his lap, holding you close, pushing inside you with aching slowness. 'You're the only good thing. The only real thing.' He buries his face in your neck and fucks you like he's praying.")
            )),
        StoryArc("arc_19", "19", "Permanent Ink",
            "Getting a tattoo from Silas turns into an experience that leaves a mark far deeper than ink.",
            listOf(
                StoryChapter("19_c1", "Consultation", "The tattoo session takes an unexpected turn.", 1,
                    startingPrompt = "The needle hums. Silas is leaning over you, your shirt off, his breath warm on your skin. But the tattoo gun is off now, set aside. 'I can't focus with you laid out like this.' He lowers the chair into a recline, his hand sliding under your waistband. 'Let me mark you in a different way first.'"),
                StoryChapter("19_c2", "Healing", "Silas tends to you — and himself — after the ink settles.", 2, 15, 20,
                    startingPrompt = "The fresh tattoo is wrapped. Silas leads you to the back room, laying you down gently. He traces the bandage with his fingers. 'You let me mark you forever.' He kisses along your jaw, his cock pressing against your thigh. 'Now I want to worship every inch of the canvas I've been given.'")
            )),
        StoryArc("arc_20", "20", "Last Call",
            "A night at Bastian's speakeasy ends with the real party starting after hours.",
            listOf(
                StoryChapter("20_c1", "Behind the Velvet Rope", "Bastian's private office becomes a playground.", 1,
                    startingPrompt = "The hidden door clicks shut, muffling the jazz. Bastian's office is all dark wood and amber liquor. He pours two fingers of whiskey, then pulls you against him, his expensive suit rough against your skin. 'Everyone out there wants a taste of what I have. But you —' he bites your earlobe, '— you get the whole bottle.' His hand finds your belt."),
                StoryChapter("20_c2", "The Back Room", "Bastian's private sanctuary reveals a different side of him.", 2, 20, 20,
                    startingPrompt = "The back room is velvet and candlelight, a bed that takes up most of the space. Bastian sheds his suit jacket, revealing a harness underneath. He kneels, presenting the leather straps to you. 'I run this place. I control everything.' He looks up, eyes dark with want. 'Tonight, I want you to control me.'")
            )),
        // --- Additional arcs ---
        StoryArc("arc_21", "1", "Brushes & Bare Skin",
            "Kai wants to explore a new medium — and you're the only model he has in mind.",
            listOf(
                StoryChapter("21_c1", "Body Canvas", "Kai's newest project involves painting directly on your skin.", 1,
                    startingPrompt = "The studio is warm, incense burning. Kai has laid out fresh paints and a wide, soft brush. He's shirtless, already hard. 'I want to paint on you this time. Every muscle, every curve.' He dips the brush in gold and traces it along your collarbone, then lower. 'And when I'm done painting you, I'm going to lick every stroke off.'"),
                StoryChapter("21_c2", "The Exhibition", "Kai wants to share his living masterpiece with a private audience.", 2, 20, 20,
                    startingPrompt = "A single spotlight. A velvet chaise. Kai positions you under the light, your body painted in swirling gold and rose. He circles you like a predator. 'I want them to see you like this — art and flesh.' His hand slides between your legs. 'But when they leave, you're mine to clean up with my tongue.'")
            )),
        StoryArc("arc_22", "2", "Locked In",
            "A sudden storm traps Ethan and you inside his cabin with nothing but a fire and each other.",
            listOf(
                StoryChapter("22_c1", "The Storm", "The power goes out and Ethan's cabin becomes your world.", 1,
                    startingPrompt = "Thunder rattles the windows. The lights flicker and die. Ethan's already stripped by the fire, the glow tracing every ridge of his body. 'Looks like we're stuck here.' He pats the fur rug beside him. 'Might as well make good use of the time.' His cock is already thick in the firelight. 'Come here and let me warm you up.'"),
                StoryChapter("22_c2", "Morning Light", "The storm passes but Ethan isn't ready to let you go.", 2, 20, 15,
                    startingPrompt = "Sunlight streams through the cabin windows. Ethan has you pinned beneath him, still inside you from the night before. He's barely moved, just staying deep. 'I've been awake for an hour just like this.' He rocks his hips slowly. 'Watching you sleep. Feeling you around me. I could stay here forever.'")
            )),
        StoryArc("arc_23", "7", "Sparring Session",
            "Training with Malakai in the ring ends in a very different kind of submission.",
            listOf(
                StoryChapter("23_c1", "The Ring", "Sparring with Malakai gets heated in more ways than one.", 1,
                    startingPrompt = "The gym is empty, the ring lit from above. Malakai circles you in his fight shorts, body slick with sweat. 'Let's see what you've got.' He feints, then pins you against the ropes, his hard body pressed full against yours. He grins, feeling exactly what the contact does to you. 'Looks like you're distracted. Maybe I should teach you a different kind of hold.'"),
                StoryChapter("23_c2", "Tap Out", "Malakai teaches you the meaning of submission.", 2, 15, 20,
                    startingPrompt = "You're pinned face-down on the mat. Malakai's weight is on top of you, his cock pressing against your ass through the thin shorts. 'You can tap out anytime.' He grinds against you slowly. 'But I don't think you want to.' He pulls your shorts down. 'I'm going to fuck you right here on the mat. And you're going to thank me for it.'")
            )),
        StoryArc("arc_24", "13", "Private Reserve",
            "Sebastian takes you into his wine cellar for a tasting that becomes far more intoxicating.",
            listOf(
                StoryChapter("24_c1", "The Cellar", "Sebastian's private wine cellar is dark, cool, and very secluded.", 1,
                    startingPrompt = "The cellar door swings shut behind you, plunging you into candlelit dimness. Sebastian selects a bottle with deliberate care, but his eyes never leave you. 'The 2005 Barolo. Full-bodied, intense, meant to be savored slowly.' He pours two glasses, then sets them aside. 'But I've found something I'd rather taste.' He drops to his knees and undoes your fly."),
                StoryChapter("24_c2", "Vintage", "Sebastian pairs the finest bottle with the finest dessert — you.", 2, 15, 20,
                    startingPrompt = "The last bottle is open, the air thick with aged oak and spice. Sebastian has you across the tasting table, a splash of wine cooling on your chest. He laps it up slowly. 'They say wine improves with age.' He pushes inside you, deep and deliberate. 'But you — you get better every time I have you.'")
            )),
        StoryArc("arc_25", "20", "Members Only",
            "Bastian invites you to the speakeasy's most exclusive night — where anything goes.",
            listOf(
                StoryChapter("25_c1", "The Door", "Bastian leads you past a hidden door into a room you've never seen.", 1,
                    startingPrompt = "The music is a low, sensual thrum. Bastian leads you through a hidden panel into a room of velvet banquettes and dim red light. Other guests are here, but their attention is elsewhere — on each other. Bastian's hand finds the back of your neck. 'This is the members-only night.' His voice is honey and smoke. 'Tonight, anything you want. I'll give it to you right here.'"),
                StoryChapter("25_c2", "Private Viewing", "Bastian gives the crowd a show they won't forget.", 2, 25, 25,
                    startingPrompt = "The room has gone quiet. All eyes are on the velvet chaise where Bastian has you laid out. He's slow, deliberate, making sure everyone watches. 'Look at them,' he whispers, thrusting deep. 'They're all jealous. They all wish it was them.' He grips your hips harder. 'But you're the only one I want seeing stars tonight.'")
            ))
    )


    override fun getStoriesForCharacter(characterId: String): Flow<List<StoryArc>> {
        return flowOf(allArcs.filter { it.characterId == characterId })
    }

    override fun getProgressForCharacter(characterId: String): Flow<List<StoryProgress>> {
        return storyDao.getProgressForCharacter(characterId).map { list -> list.map { it.toExternal() } }
    }

    override suspend fun updateProgress(progress: StoryProgress) {
        storyDao.insertProgress(progress.toEntity())
    }

    override suspend fun getStoryArc(arcId: String): StoryArc? {
        return allArcs.find { it.id == arcId }
    }
}
