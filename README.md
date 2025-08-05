# csc413-zombiegame
<img src="ZombieGame/resources/vfx/title.png" width="300">

| Student Information |                 |
|:-------------------:|-----------------|
|  Student Name       | Roxana del Toro |
|  Student Email      | rxdt@sfsu.edi   |


## Purpose of jar Folder 
The jar folder will store the built jar
`out/artifacts/tankgame_rxdt_jar/tankgame-rxdt.jar`

`THIS FOLDER CAN NOT BE DELETED OR MOVED`

# Required Information when Submitting Tank Game

## Version of Java Used: 24.0.1

## IDE used: 
### IntelliJ IDEA 2025.1.2 (Ultimate Edition) on Macbook Pro

## Steps to Import project into IDE:
`git clone git@github.com:csc413-SFSU-SU2025/tankgame-rxdt.git`

## Steps to Manually Build Project:
```
>  cd tankgame-rxdt
>  mkdir -p out
>  javac -d out $(find ZombieGame/src -name "*.java")
>  cp -r ZombieGame/resources/* out/
```
 
## Steps to run Project:
```
>  java -cp out zombiegame.Launcher
```
#### Or run the JAR directly
```
>  java -jar tankgame-rxdt/out/artifacts/tankgame_rxdt_jar/tankgame-rxdt.jar
```

## Controls to play Zombie Game:

|               | Player 1 - Green | Player 2 - Red |
|---------------|------------------|----------------|
|  Forward      | W                | up arrow       |
|  Backward     | S                | down arrow     |
|  Rotate left  | A                | left arrow     |
|  Rotate Right | D                | right arrow    |
|  Shoot        | space            | enter          |

