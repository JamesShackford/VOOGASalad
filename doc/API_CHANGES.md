Canvas: getXScrollAmount, getYScrollAmount
Networking: isConnected()

Level: Getters and setters for the camera and background were added. 
This was because it was not known from the beginning how the camera
and background would be stored. Also, all the timer-related methods
were removed, since we realized that Timeline methods in the game loop
were sufficient to pause/resume, etc. the game.

LevelManager: Deleted method to save game, since the Game Player
ended up taking care of saving the game. Added levelNumberInGame for
error checking (was not obvious this would be necessary before), and
resetCurrentLevel() since it was not originally obvious whether Game Data
would reinitialize levels automatically.
