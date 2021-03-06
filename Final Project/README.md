## Moral Machine

The idea of Moral Machines is based on the Trolley Dilemma, a fictional scenario presenting a decision maker with a moral dilemma:  choosing ”the lesser of two evils”.The scenario entails an autonomous car whose brakes fail at a pedestrian crossing. As it is too late to relinquish control to the car’s passengers, the car needs to make a decision based on the facts available about the situation. **Figure 1** shows an example  scenario. In this project, you will create an Ethical Engine, a program designed to explore different scenarios, build an algorithm to decide between the life of the car’s passengersvs, the life of the pedestrians, audit your decision-making algorithm through simulations, and allow users of your program to judge the outcomes themselves.

![Demo 1](https://github.com/ruichenaero/COMP90041-Assignment/blob/master/Final%20Project/Moralmachine_example.PNG)

**Figure 1:**  Scenario example:  a self-driving car approaches a pedestrian crossing but its breaks fail.  Youralgorithm needs to decide between two cases.Left:  The car will
continue ahead and drive through thecrossing  resulting  in  one  elderly  man,  one  pregnant  woman,  one  boy,  and  one  dog  losing  their  lives.Right:The car will swerve
and crash into a concrete barrier resulting in the death of its passengers:  onewomen, one man, and one baby.  Note that the pedestrians abide by the law as they are crossing on
agreen signal (image source:  http://moralmachine.mit.edu/)

## Game Start

You can run [`EthicalEngine.java`](https://github.com/ruichenaero/COMP90041-Assignment/blob/master/Final%20Project/EthicalEngine.java) using command line to start the game.

You can use the arguments following:

`-c` or `--config`          Optional: path to config file <br>
`-h` or `--help`        Print Help (this message) and exit <br>
`-r` or `--results`     Optional: path to results log file <br>
`-i` or `--interactive` Optional: launches interactive mode <br>
