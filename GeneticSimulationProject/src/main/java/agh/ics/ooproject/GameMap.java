package agh.ics.ooproject;


import java.util.ArrayList;
import java.util.List;

public class GameMap {
    MapType type;
    int width;
    int height;
    int noPlants;
    int plantEnergy;
    int noNewPlants;
    AbstractPlantGenerator plantGenerator;
    int noAnimals;
    int startingEnergy;
    int sufficientEnergy;
    int consumedEnergy;
    int minimalMutations;
    int maximalMutations;
    int genLength;
    MutationType mutationType;
    BehaviourType behaviour;
    Cell[][] grid;
    List<ElementAnimal> animals;
    int day;

    public GameMap(MapType type, int width, int height, int noPlants, int plantEnergy, int noNewPlants,
                   AbstractPlantGenerator plantGenerator, int noAnimals, int startingEnergy,
                   int sufficientEnergy, int consumedEnergy, int minimalMutations, int maximalMutations,
                   MutationType mutationType, int genLength, BehaviourType behaviour){
        this.type = type;
        this.width = width;
        this.height = height;
        this.noPlants = noPlants;
        this.plantEnergy = plantEnergy;
        this.noNewPlants = noNewPlants;
        this.plantGenerator = plantGenerator;
        this.noAnimals = noAnimals;
        this.startingEnergy = startingEnergy;
        this.sufficientEnergy = sufficientEnergy;
        this.consumedEnergy = consumedEnergy;
        this.minimalMutations = minimalMutations;
        this.maximalMutations = maximalMutations;
        this.mutationType = mutationType;
        this.behaviour = behaviour;
        this.genLength = genLength;
        this.day = 0;

        this.grid = new Cell[height][width];
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                this.grid[i][j] = new Cell(this, new Position(i, j));
            }
        }
    }

    public Cell getCellAt(Position position){
        return this.grid[position.y][position.x];
    }

    public void initializeAnimals(){
        int randomX;
        int randomY;
        ElementAnimal animal;
        for (int i = 0; i < noAnimals; i++){
            randomX = (int) Math.floor(Math.random()*this.width);
            randomY = (int) Math.floor(Math.random()*this.height);
            animal = new ElementAnimal(this, new Position(randomX, randomY), 0,
                    this.startingEnergy, this.sufficientEnergy, this.consumedEnergy, this.mutationType, this.genLength, this.behaviour);
            this.grid[randomY][randomX].addElement(animal);
            this.animals.add(animal);
        }
    }
    public void initializeGrass(){
        this.plantGenerator.initialize();
    }

    public void deleteDead(){
        List<ElementAnimal> toDelete = new ArrayList<>();
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                for (int k = 0; k < this.grid[i][j].animals.size(); k++) {
                    if(!this.grid[i][j].animals.get(k).alive){
                        toDelete.add(this.grid[i][j].animals.get(k));
                    }
                }
                for (ElementAnimal element : toDelete) {
                    this.grid[i][j].animals.remove(element);
                }
            }
        }
    }
    public Border isValidPosition(Position position){
        if (position.x < this.width && position.x > 0 && position.y < this.height && position.y > 0){
            return Border.Inside;
        }
        if (position.x > this.width && position.y < this.height && position.y > 0){
            return Border.Right;
        }
        if (position.x < 0 && position.y < this.height && position.y > 0){
            return Border.Left;
        }
        if (position.x < this.width && position.x > 0 && position.y > this.height){
            return Border.Up;
        }
        if (position.x < this.width && position.x > 0 && position.y < 0){
            return Border.Down;
        }
        return Border.Corner;
    }
    public void updateAnimalPositions(){
        ElementAnimal animal;
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                for (int k = 0; k < this.grid[i][j].animals.size(); k++) {
                    animal = (ElementAnimal) this.grid[i][j].animals.get(k);
                    animal.move();
                }
            }
        }
    }
    public void consumption(){
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                this.grid[i][j].eatGrass();
            }
        }
    }
    public void procreation(){
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                this.grid[i][j].procreate();
            }
        }
    }
}