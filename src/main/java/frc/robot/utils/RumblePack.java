package frc.robot.utils;

import frc.robot.utils.RumbleSubsystem.Priority;

public class RumblePack {
        public double strength;
        public double duration;
        public Priority priority;

        public RumblePack() {
            strength = 0;
            duration = 0;
            priority = Priority.NONE;
        }

        public RumblePack(double strength, double duration, Priority priority) {
            this.strength = strength;
            this.duration = duration;
            this.priority = priority;
        }
}