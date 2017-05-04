# voogasalad

**Team DuWaldorf**: Game Authoring Engine Project.

**Genre**: Scrolling Platformer.

---

### Names

- Kyle Finke (kwf10)
- Elliott Bolzan (eab91)
- Mina Mungekar (mem94)
- Jimmy Shackford (jas199)
- Jay Doherty (jld60)
- Jesse Yue (jty4)
- Matthew Barbano (meb100)
- Nikita Zemlevskiy (naz7)
- Dennis Ling (dl186)
- Michael Li (mxl3)

---------------

### Dates & Times

- **Start Date**: 21 March 2017.

- **End Date**: 30 April 2017.

- **Hours of Work**: 900.


### Each Person's Role in Developing the Project

**Kyle**

I worked primarily in the game engine. I created the algorithm and code for detecting when a collision happens and which side the collision occurs on for the two entities involved in the collision. I also worked on designing and coding Actions, Entities, and Events for the game. Additionally, I assisted in the testing and debugging the reflection used to create the lists of Actions, Entities, and Events displayed to the user in the front-end by looping through these classes in the back-end. I was also worked on exception handling within the engine. Outside of coding, I tested different game features and provided feedback to the respective areas of the team. I also organized the first demonstration and acted as the primary-spokesperson.

**Jay**

I worked on a wide variety of different parts of the project. Originally I was in charge of the interface between the game engine and the game player, so translating the back-end Entities into JavaFX images to display on screen, which is what the GraphicsEngine class does. I learned how the game engine worked and contributed some to figuring out the flow of the game loop and popping up transition screens between levels. I also took primary responsibilities for making the scrolling camera and the background for the game. Since I understand the camera I helped make Actions for making it follow the player. In the process of adding these I ended up helping with formatting what gets saved in the XML files by game data. I helped a lot with making the file paths saved by game data be flexible enough that a game made on one system should be fully playable/editable on another. Besides all of that I've generally tried to help solve bugs within game data and the game engine since I have worked in both areas.

**Jesse**

I worked primarily on the game player. I created the menus in the player and coded the logic for populating them, along with helping to create the player itself. I also helped around in the authoring environment, game engine, and game data. Created a way to create and display game info in the authoring environment, helped make achievement entities to make displaying and updating achievements possible, and helped work on save states to save the game at any particular point. I also did game testing and helped debug various issues.

**Mina**

I primarily focused on the Game Authoring Environment. I worked on a variety of features in the front-end, including game layering, which involved developing the tools the user could utilize to create a foreground, background, and any number of intermediate layers. I set up the data structures to track the user's drag-drop action to different layers and implemented an error handling system to enforce a minimum number of layers in the game. I also developed a help system for the Game Authoring Environment, creating the Authoring Tutorial, a quick tour of the GAE that the user can select through the Help Menu. As part of developing a user-friendly help system, I also installed tooltips and info boxes at various locations in order to guide the user through the process of developing a game. Beyond coding, I have offered my help on various tasks whenever I've completed my part and made an effort to take in as much feedback as possible from my teammates.

**Elliott Bolzan (eab91)**

During this project, I worked on the following parts:

- Created the Game Authoring Environment visual layout.
- Creating the Panel in the Authoring Environment.
- Allowed for the ability to create Entities, Actions, and Events graphically.
- Designed the icon and logos for our project. 
- Designed the project's visual identity and wrote the CSS files.
- Implemented the Menus in the starting window and Game Authoring Environment.
- Wrote a utility, `polyglot`, that translates a ResourceBundle into any language at runtime.
- Integrated another team's networking utility in our project.
- Set up a chat system in the Game Authoring Environment that functions between machines.
- Helped design Actions and Events towards the end of the project (specifically, `BooleanParser`, which lets the designer combine `Events`).

**Dennis**

I worked primarily on game data. I worked with Michael to convert our games from a list of entities into an XML file that we saved into a folder initially, and then a zip file. I also worked with Michael to load our games after saving them, so we had to convert our XML file into a list of entities to supply to the backend once again. I made the change to compress all of the files necessary in saving a game so we could save the game into one .zip/.vs file. I also made the changes to load from just one .vs/.zip file, which involved uncompressing all of the files.

**Matthew**

I focused mostly on the Game Engine, with a little overlap into the Game Player. Within the Game Engine,
I implemented logic on a high-level (e.g. recording all levels, the game loop, etc.), though I did also
work some on the lower-level creating Events and Actions. During the first sprint, I worked on the LevelManager, Level, and SelectionGroup classes to allow the basics of levels to function. I also used
the Observer design pattern for user input, collisions, and the timer passing a certain time. I made the timer itself, and worked with Jay to create the Scorebar. I spent most of my time implementing the Game Loop using the Strategy Design Pattern, which included several substantial classes for stepping through levels, transition screens for displaying messages like "Game Over", and significant debugging. I implemented Actions such as LevelLostAction and LevelNextAction. In the second sprint, I began by spending much time
debugging game loop issues, including reinitialization/cloning after a death and getting multiple levels
to work. After that, I implemented a screen for selecting which level to play, implemented a "unlocked" level system, worked with Game Data to get game player saving to work, and made lives/the timer modifiable from the Authoring Environment. For Events/Actions, I implemented the TraverseLinePathAction for allowing
Entities to move in a straight line forever, FaceTowardsAction for enemies attacking the hero, mouse events,
actions for winning a level, and several more.

**Jimmy**

I focused on the canvas portion of the Game Authoring environment. I added the following parts:
- Allow users to drag-and-drop entities from the side-panel onto the canvas.
- Allow users to drag entities around on the canvas to move them
- Allow users to resize an entity by dragging on any side/corner.
- Added an undo-redo function so users can redo mistakes.
- Added live-editing so that multiple users can connect to the same server and edit the same game. This requires the two screens to be synchronized immediately after joining the server.
- Added feature so users can scroll into the canvas with the mouse scroll wheel.
- Users can drag the screen to pan around the canvas.
- Added copy-paste functionality (ctrl-c, ctrl-v), select-all functionality (ctrl-a)
- Allow users to select multiple entities at once and drag them all around at the same time.
- Added multiple layers and multiple level functionality

**Nikita Zemlevskiy (naz7)**
I worked on the following parts during this project:
- Design and implement basic backend structure for running a game
- Create numerous entities, events and actions according to the above structure
- Extra features implemented using this structure include:
    - Random generation of levels
    - Achievements
    - Combining events to react to complex conditions (`BooleanEvent`)
- Wrote a utility, `FacebookPoster`, that posts images and messages on facebook, taking care of oauth and logging in.
- Create backbone of serialization and deserialization functionality 
- Created ways for communication at a high level between game data, player, authoring and the engine.

### Resources Used

* http://vignette2.wikia.nocookie.net/nintendo/images/d/de/Princess_Peach_%28Fortune_Street%29.png/revision/latest?cb=20130625131449&path-prefix=en
* https://i.giphy.com/DGaZTLF390Z0s.gif
* https://i.giphy.com/hbxHb36GFCB5m.gif
* https://img.gifamerica.com/22cc9b1848e768b59f1be3c4f485655d_download-upload-gif-mario-walking-gif-8-bit_256-256.gif
* https://orangemangocraft64.files.wordpress.com/2012/03/doodle_monsters.png
* https://vignette2.wikia.nocookie.net/doodlejumpiphone/images/7/79/Doodle_Platforms.png/revision/latest?cb=20100411232504
* http://vignette2.wikia.nocookie.net/doodle-jump/images/5/5c/Doodler.png/revision/latest?cb=20130403202424
* http://vignette1.wikia.nocookie.net/fantendo/images/3/3f/SuperMushroom.png/revision/latest?cb=20130803025706
* https://cdn.instructables.com/FFB/WMFG/FWBNM65X/FFBWMFGFWBNM65X.MEDIUM.jpg
* http://ian-albert.com/games/super_mario_bros_maps/mario-1-1.gif
* http://stackoverflow.com/questions/31022269/collision-detection-between-two-rectangles-in-java
* https://docs.oracle.com/javase/8/javafx/api/javafx/scene/layout/Pane.html
* http://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html
* http://www.javaworld.com/article/2077258/learn-java/observer-and-observable.html
* http://stackoverflow.com/questions/31148690/get-real-position-of-a-node-in-javafx
* https://docs.oracle.com/javase/tutorial/java/javaOO/enum.html
* http://www.oodesign.com/
* Stack Overflow
* JavaFX 8 Documentation: https://docs.oracle.com/javase/8/javafx/api/toc.htm
* http://stackoverflow.com/questions/19383953/is-it-possible-to-evaluate-a-boolean-expression-for-string-comparions
* https://rterp.wordpress.com/2015/09/01/creating-custom-animated-transitions-with-javafx/
* http://www.codejava.net/java-se/file-io/programmatically-extract-a-zip-file-using-java
* http://stackoverflow.com/questions/30183634/how-to-zip-files-and-folders-in-java
* http://stackoverflow.com/questions/16925612/how-to-resize-component-with-mouse-drag-in-javafx
* http://stackoverflow.com/questions/16680295/javafx-correct-scaling
* http://stackoverflow.com/questions/29506156/javafx-8-zooming-relative-to-mouse-pointer
* http://stackoverflow.com/questions/18982623/restfb-post-as-page-step-by-step-working-example
* http://restfb.com/documentation/
* http://x-stream.github.io/tutorial.html
* http://stackoverflow.com/questions/15519626/how-to-get-all-classes-names-in-a-package
* http://www.vgmaps.com/Reviews/Review-DS-NewSuperMarioBros.htm
* https://www.mariowiki.com/File:8-Castle2nsmbwii.png
* http://mario.wikia.com/wiki/Koopa_Paratroopa
* https://www.mariowiki.com/File:SwooperSMG.png 
* https://www.mariowiki.com/File:Bowser_-_Mario_Party_10.png
* http://www.suppermariobroth.com/post/76693190799/bowsers-castle-background-from-new-super-mario
* http://mario.wikia.com/wiki/File:Question_Block_Art_-_New_Super_Mario_Bros.png
* http://www.clker.com/clipart-9803.html
* https://www.nickanimationstudio.com/spongebob-squarepantshttp://www.sporistics.com/?p=asset:500543929175
* http://spongebob.wikia.com/wiki/Squidward_Tentacles


### Files Used to Start the Project (the class(es) containing main)

Our main class is located at: `src/starter/Main.java`.


### Files Used to Test the Project

All files in `src/testers`:

* `EntityConverterTest.java`.
* `TestGameEngineStartup.java`.
* `TestGameMarioMove.java`.
* `TestHTML.java`.
* `TestMultipleLevels.java`.

### Any data or resource files required by the project (including format of non-standard files)

All data and resource files are contained in the folders `src/resources` (resources used by code), `data/resources` (resources that can be used to make games) and `src/polyglot/resources` (resources used by Polyglot).

### Any information about using the program (i.e., command-line/applet arguments, key inputs, interesting example data files, or easter eggs)

See extra features below for a comprehensive list of cool features.

### Any known bugs, crashes, or problems with the project's functionality

None.

### Any extra features included in the project

* Chat between two computers.
* Simultaneous editing of a game between two computers.
* Undo-redo button in the game-authoring environment
* Tutorials for making a game.
* Automatic translation of the project into hundreds of languages at runtime.
* The ability to post status updates to Facebook.
* Parse Boolean expressions for Events. Trigger complex combinations of Events using &&, ||, and !.
* The ability to generate levels in a game randomly

### Impressions of the Assignment

**Kyle Finke (kwf10)**

The project was a good culmination for that class because it provided ample opportunity to apply the design concepts we learned during the course. The size of the 
teams was also a useful experience because it requires that everyone rely on each other because no one person can take over the entire assignment. It could improve the 
project to meet with our TAs more often to show off our project and get advice because although Piazza is useful for answering questions, it doesn't provide the same help
that having someone outside of the team look at the project. Going off of that idea, the user testing was a great tool and experience because it truly demonstrates the areas
of the project that need improvement or clarification to allow someone who has not worked on the project to actually use it.

**Elliott Bolzan (eab91)**

The project, while a considerable amount of work, was enjoyable. The final product is considerably more impressive than the previous projects, making the effort more worthwhile. Additionally, the challenge of working for an extended period of time with a large number of people was interesting: I developed better relationships with my team members than during previous projects.

The most interesting parts of the project were, by far, designing the utilities and implementing the extra features. In terms of class work, the lab during which we let other teams use our product was very beneficial. We were able to identify a number of problems and it pushed people to work more on our project.

One thing I would change in the future: maybe it would be beneficial to not dissuade people from trying to implement several game genres. Our project seems to cover a few, and had we not been told to focus exclusively on one game genre during our first coding sprint, we probably could have designed games for all genres. That would have been a great experience!

**Jimmy Shackford (jas199)**

The project was a great way to show off what we learned about teamwork and code design principles. The final product was massive, and it definitely shows how much effort we put into it. For this project specifically, we focused our design on being as user-friendly as possible, something that the other projects didn't really require.

Through this project, us teammates learned how to evenly divide up work and place trust in each other to achieve a great final product. My favorite part of this project was trying to make the Game Authoring Environment as user-friendly as possible because it required me to forget all of my knowledge of the project and place myself in the shoes of a beginning user. It really opened my eyes to making something that's fun and intuitive to use.

One change that I would make in the future is emphasizing how important the user experience is, and urging people to make their designs more user-friendly, perhaps by incorporating that into the grade.

**Nikita Zemlevskiy (naz7)**

This was a much bigger-scale project than the ones we had done previously in this class. The final product was much more interesting and impressive than the previous ones, making our effort more enjoyable. The power of the product that we made was truly baffling, we realized that our platform could be used to make a vast variety of games, not just scrolling platformers. I think the project shows the effort our team put into it. Working with such a large team was a challenge, both in terms of communication and organization and in terms of getting people to do their work.

I think designing utilities for the rest of the class to use was a very interesting part of the project. I believe this is a truly useful skill. It puts more pressure on the coder to make sure their code is readable, usable and well-written. I think the lab during which people from other teams came and tested our code was very helpful; we were able to find a considerable amount of bugs and incorporate our classmates' advice into our project, making for a better end result.

One suggestion I would make is to consider to move the utility requirement up to the first sprint. It might be challenging to come up with a useful util so soon, but it would give people more time to incorporate them into their project, again making for a more impressive result. Additionally, it seems possible to cover multiple game genres in one project; having that aim may produce a more flexible design.

**Matthew Barbano (meb100)**

Although this project was extremely challenging, I enjoyed the fact that it let me experience what it is like to work in a large software engineering team with a large-scale code base. An interesting point was the division into subteams, since unlike previous projects, the whole team did not meet every time code was added. Also, I found myself working more than expected with members of different subteams, particularly the Game Player (for interaction with the game loop) and Game Data (for level loading and saving). Overall, it was a significant challenge but definitely worthwhile.
