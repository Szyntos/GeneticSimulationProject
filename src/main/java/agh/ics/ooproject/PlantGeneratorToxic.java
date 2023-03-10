package agh.ics.ooproject;

import java.util.*;

public class PlantGeneratorToxic extends AbstractPlantGenerator{

    public PlantGeneratorToxic(GameMap map){
        this.map = map;
    }
    public void addGrass(int n){
        List<Cell> cells = new ArrayList<>();
        for (int i = 0; i < this.map.height; i++){
            cells.addAll(Arrays.asList(this.map.grid[i]).subList(0, this.map.width));
        }

        Comparator<Cell> byDeaths = Comparator.comparingInt((Cell x) -> x.deaths);
        Collections.shuffle(cells);
        cells.sort(byDeaths);

        List<Cell> preferableCells = new ArrayList<>();
        List<Cell> notPreferableCells = new ArrayList<>();
        for (int i = 0; i < this.map.height*this.map.width; i++){
            if (!cells.get(i).hasGrass){
                if (preferableCells.size() < n){
                    preferableCells.add(cells.get(i));
                }else{
                    notPreferableCells.add(cells.get(i));
                }
            }
        }
        int noFreeCells = notPreferableCells.size() + preferableCells.size();
        Pareto(n, noFreeCells, preferableCells, notPreferableCells);
    }

}
