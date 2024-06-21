package io.ghast.hitdelayfix.commands;

import io.ghast.hitdelayfix.HitDelayFix;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import java.util.Collections;
import java.util.List;

public class HitDelayDebugCommand implements ICommand {
    @Override
    public String getCommandName() {
        return "hitdelaydebug";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Usage: /hitdelaydebug <on/off/reset>";
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.emptyList();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length != 1 || (!args[0].equalsIgnoreCase("off") && !args[0].equalsIgnoreCase("on") && !args[0].equalsIgnoreCase("reset"))) {
            sender.addChatMessage(new ChatComponentText("Invalid Arguments. Usage: /hitdelaydebug <on/off/reset>"));
            return;
        }

        if (args[0].equalsIgnoreCase("reset")) {
            HitDelayFix.INSTANCE.affectedAttackCounter.reset();
            sender.addChatMessage(new ChatComponentText("Debug counters has been reset."));
            return;
        }

        HitDelayFix.INSTANCE.affectedAttackCounter.setEnabled(args[0].equalsIgnoreCase("on"));

        sender.addChatMessage(new ChatComponentText(String.format("HitDelayFix debugging has been %s.", HitDelayFix.INSTANCE.affectedAttackCounter.isEnabled() ? "enabled" : "disabled")));
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}