# csc413-zombiegame


| Student Information |                 |
|:-------------------:|-----------------|
|  Student Name       | Roxana del Toro |
|  Student Email      | rxdt@sfsu.edi   |


## Purpose of jar Folder 
The jar folder will store the built jar of your term project.

`NO SOURCE CODE SHOULD BE IN THIS FOLDER. DOING SO WILL CAUSE POINTS TO BE DEDUCTED

`THIS FOLDER CAN NOT BE DELETED OR MOVED`

# Required Information when Submitting Tank Game

## Version of Java Used: 24.0.1

## IDE used: IntelliJ IDEA 2025.1.2 (Ultimate Edition) on Macbook Pro

## Steps to Import project into IDE:
`git clone git@github.com:csc413-SFSU-SU2025/tankgame-rxdt.git`

## Steps to Build Your Project:
```
>  cd tankgame-rxdt
>  mkdir -p out
>  javac -d out $(find ZombieGame/src -name "*.java")
>  cp -r ZombieGame/resources/* out/
```
 
## Steps to run your Project:
```
>  java -cp out zombiegame.Launcher
```

## Controls to play your Game:

|               | Player 1 - Green | Player 2 - Red |
|---------------|------------------|--------------|
|  Forward      | W                | up arrow     |
|  Backward     | S                | down arrow   |
|  Rotate left  | A                | left arrow   |
|  Rotate Right | D                | right arrow  |
|  Shoot        | space            | enter        |

<!-- You may add more controls if you need to. -->
