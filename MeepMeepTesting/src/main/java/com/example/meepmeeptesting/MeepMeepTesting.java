package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 16)
                .setDimensions(17.5,16.5)
                .build();

        /*RED
        Left test: new Vector2d(6,-34),Math.toRadians(135)
                  new Vector2d(48,-30)
        Center test: new Vector2d(12,-30),Math.toRadians(90)
                  new Vector2d(48,-36)
        Right test: new Vector2d(18,-34),Math.toRadians(45)
                  new Vector2d(48,-42)
         */

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(12, 61, Math.toRadians(270)))
                .lineToY(48)
                .splineTo(new Vector2d(6,34),Math.toRadians(225))
                .waitSeconds(.5)
                .lineToY(36)
                .waitSeconds(1)
                .lineToY(37)
                .splineTo(new Vector2d(48,42),Math.toRadians(0))
                .lineToX(46)
                .strafeTo(new Vector2d(46,60))
                .strafeTo(new Vector2d(54,60))
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}