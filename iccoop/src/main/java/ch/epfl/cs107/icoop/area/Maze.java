package ch.epfl.cs107.icoop.area;

import ch.epfl.cs107.icoop.actor.foe.BombFoe;
import ch.epfl.cs107.icoop.actor.foe.HellSkull;
import ch.epfl.cs107.icoop.actor.object.ElementalWall;
import ch.epfl.cs107.icoop.actor.object.Explosif;
import ch.epfl.cs107.icoop.actor.object.Heart;
import ch.epfl.cs107.icoop.actor.object.PressurePlate;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.List;

public class Maze extends ICoopArea {
    private DialogHandler jeu;

    public Maze(DialogHandler jeu) {
        this.jeu = jeu;
    }


    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));
        HellSkull hellSkull1 = new HellSkull(this, Orientation.RIGHT, new DiscreteCoordinates(12, 33), List.of("physique", "eau"));
        registerActor(hellSkull1);
        HellSkull hellSkull2 = new HellSkull(this, Orientation.RIGHT, new DiscreteCoordinates(12, 31), List.of("physique", "eau"));
        registerActor(hellSkull2);
        HellSkull hellSkull3 = new HellSkull(this, Orientation.RIGHT, new DiscreteCoordinates(12, 29), List.of("physique", "eau"));
        registerActor(hellSkull3);
        HellSkull hellSkull4 = new HellSkull(this, Orientation.RIGHT, new DiscreteCoordinates(12, 27), List.of("physique", "eau"));
        registerActor(hellSkull4);
        HellSkull hellSkull5 = new HellSkull(this, Orientation.RIGHT, new DiscreteCoordinates(12, 25), List.of("physique", "eau"));
        registerActor(hellSkull5);
        HellSkull hellSkull6 = new HellSkull(this, Orientation.RIGHT, new DiscreteCoordinates(10, 34), List.of("physique", "eau"));
        registerActor(hellSkull6);
        HellSkull hellSkull7 = new HellSkull(this, Orientation.RIGHT, new DiscreteCoordinates(10, 32), List.of("physique", "eau"));
        registerActor(hellSkull7);
        HellSkull hellSkull8 = new HellSkull(this, Orientation.RIGHT, new DiscreteCoordinates(10, 30), List.of("physique", "eau"));
        registerActor(hellSkull8);
        HellSkull hellSkull9 = new HellSkull(this, Orientation.RIGHT, new DiscreteCoordinates(10, 28), List.of("physique", "eau"));
        registerActor(hellSkull9);
        HellSkull hellSkull10 = new HellSkull(this, Orientation.RIGHT, new DiscreteCoordinates(10, 26), List.of("physique", "eau"));
        registerActor(hellSkull10);

        Explosif explosif = new Explosif(this, Orientation.DOWN, new DiscreteCoordinates(6, 25));
        registerActor(explosif);

        Heart coeur1 = new Heart(this, Orientation.DOWN, new DiscreteCoordinates(15, 18));
        registerActor(coeur1);
        Heart coeur2 = new Heart(this, Orientation.DOWN, new DiscreteCoordinates(16, 19));
        registerActor(coeur2);
        Heart coeur3 = new Heart(this, Orientation.DOWN, new DiscreteCoordinates(14, 19));
        registerActor(coeur3);
        Heart coeur4 = new Heart(this, Orientation.DOWN, new DiscreteCoordinates(14, 17));
        registerActor(coeur4);

        PressurePlate plate1 = new PressurePlate(this, new DiscreteCoordinates(6, 33), Orientation.DOWN);
        registerActor(plate1);

        ElementalWall fireWallP1 = new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(6, 35), new Logic() {
            @Override
            public boolean isOn() {
                return plate1.isOff();
            }

            @Override
            public boolean isOff() {
                return plate1.isOn();
            }
        }, ElementalWall.WallType.FEU);
        registerActor(fireWallP1);

        ElementalWall fireWallP2 = new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(6, 36), new Logic() {
            @Override
            public boolean isOn() {
                return plate1.isOff();
            }

            @Override
            public boolean isOff() {
                return plate1.isOn();
            }
        }, ElementalWall.WallType.FEU);
        registerActor(fireWallP2);

        PressurePlate plate2 = new PressurePlate(this, new DiscreteCoordinates(9, 25), Orientation.DOWN);
        registerActor(plate2);

        ElementalWall fireWallP3 = new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(8, 21), new Logic() {
            @Override
            public boolean isOn() {
                return plate2.isOff();
            }

            @Override
            public boolean isOff() {
                return plate2.isOn();
            }
        }, ElementalWall.WallType.FEU);
        registerActor(fireWallP3);

        ElementalWall waterWallactif1 = new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(4, 35), Logic.TRUE, ElementalWall.WallType.EAU);
        registerActor(waterWallactif1);
        ElementalWall waterWallactif2 = new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(4, 36), Logic.TRUE, ElementalWall.WallType.EAU);
        registerActor(waterWallactif2);
        ElementalWall waterWallactif3 = new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(5, 24), Logic.TRUE, ElementalWall.WallType.EAU);
        registerActor(waterWallactif3);
        ElementalWall waterWallactif4 = new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(6, 24), Logic.TRUE, ElementalWall.WallType.EAU);
        registerActor(waterWallactif4);
        ElementalWall waterWallactif5 = new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(8, 4), Logic.TRUE, ElementalWall.WallType.EAU);
        registerActor(waterWallactif5);


        ElementalWall fireWall1 = new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(2, 34), Logic.TRUE, ElementalWall.WallType.FEU);
        registerActor(fireWall1);
        ElementalWall fireWall2 = new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(3, 34), Logic.TRUE, ElementalWall.WallType.FEU);
        registerActor(fireWall2);
        ElementalWall fireWall3 = new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(13, 4), Logic.TRUE, ElementalWall.WallType.FEU);
        registerActor(fireWall3);

//        BombFoe bombFoe1 = new BombFoe(this, Orientation.DOWN, new DiscreteCoordinates(5, 15), List.of("physique", "feu"));
//        registerActor(bombFoe1);
//        BombFoe bombFoe2 = new BombFoe(this, Orientation.DOWN, new DiscreteCoordinates(6, 17), List.of("physique", "feu"));
//        registerActor(bombFoe2);
//        BombFoe bombFoe3 = new BombFoe(this, Orientation.DOWN, new DiscreteCoordinates(10, 17), List.of("physique", "feu"));
//        registerActor(bombFoe3);
//        BombFoe bombFoe4 = new BombFoe(this, Orientation.DOWN, new DiscreteCoordinates(5, 14), List.of("physique", "feu"));
//        registerActor(bombFoe4);
    }

    public String getTitle() {
        return "Maze";
    }

    public List<DiscreteCoordinates> getStartPositions() {
        return List.of(new DiscreteCoordinates(2, 39), new DiscreteCoordinates(3, 39));
    }

}



