package org.firstinspires.ftc.teamcode.commands.intake;

import com.arcrobotics.ftclib.command.ConditionalCommand;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.PerpetualCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.Robot;

/**
 * Collect until a valid specimen is found
 */
public class intakeCollect extends SequentialCommandGroup {
    private final boolean isForTransfer;
    public intakeCollect(boolean isForTransfer) {
        IntakeSubsystem in = Robot.sys.intake;
        this.isForTransfer = isForTransfer;

        addCommands(
            new PerpetualCommand(
                // if specimen is valid or blank, continue as normal. otherwise eject
                new ConditionalCommand(
                    new InstantCommand(() -> in.setRollerSpeed(0.25)),
                    new InstantCommand(() -> in.setRollerSpeed(1)),
                    () -> this.isValid() || in.colorDetected == IntakeSubsystem.COLOR.blank
                )
            ).interruptOn(this::isValid)
        );
    }

    private boolean isValid() {
        IntakeSubsystem.COLOR c = Robot.sys.intake.colorDetected;
        // yeah this can be simplified but readability!
        return isForTransfer
            ? (c == IntakeSubsystem.COLOR.yellow || c == Robot.sys.intake.teamColor)
            : c == Robot.sys.intake.teamColor;
    }
}