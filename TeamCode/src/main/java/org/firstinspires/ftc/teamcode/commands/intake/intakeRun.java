package org.firstinspires.ftc.teamcode.commands.intake;

import com.arcrobotics.ftclib.command.ConditionalCommand;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.commands.Intake;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.Robot;

/**
 * Extend intake and pickup pixel then retract. Transfers specimen depending on isForTransfer
 */
public class intakeRun extends SequentialCommandGroup {
    public intakeRun(boolean isForTransfer) {
        IntakeSubsystem in = Robot.sys.intake;

        addCommands(
            new InstantCommand(() -> {
                in.setIntakeDist(IntakeSubsystem.fullIntakeOutTick);
                in.setWristPos(IntakeSubsystem.WRIST.transfer);
            }),
            new WaitUntilCommand(in.pid::done),
            new InstantCommand(() -> {
                in.setWristPos(IntakeSubsystem.WRIST.intake);
                in.setIntakeSpeed(1);
                in.setRollerSpeed(0.25);
            }),
            new intakeCollect(isForTransfer),
            new InstantCommand(() -> {
                in.setWristPos(IntakeSubsystem.WRIST.transfer);
                in.setIntakeSpeed(0);
                in.setRollerSpeed(0);
            }),
            new WaitCommand(250),
            new InstantCommand(() -> in.setIntakeDist(IntakeSubsystem.intakeInTick)),
            new WaitUntilCommand(in.pid::done),
            // if we are transferring, put specimen in bucket
            new ConditionalCommand(
                // transfer
                new SequentialCommandGroup(
                    new InstantCommand(() -> {
                        in.setRollerSpeed(1);
                        in.setIntakeSpeed(1);
                    }),
                    new WaitUntilCommand(() -> in.colorDetected == IntakeSubsystem.COLOR.blank),
                    new InstantCommand(() -> {
                        in.setIntakeSpeed(0);
                        in.setRollerSpeed(0);
                    })
                ),
                new InstantCommand(),
                () -> isForTransfer
            )
        );
    }
}
