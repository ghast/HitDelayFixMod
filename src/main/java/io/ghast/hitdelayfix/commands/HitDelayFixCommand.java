package io.ghast.hitdelayfix.commands;

import io.ghast.hitdelayfix.HitDelayFix;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import java.util.Collections;
import java.util.List;

public class HitDelayFixCommand implements ICommand {
    @Override
    public String getCommandName() {
        return "hitdelayfix";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Usage: /hitdelayfix <on/off>";
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.emptyList();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length != 1 || (!args[0].equalsIgnoreCase("off") && !args[0].equalsIgnoreCase("on"))) {
            sender.addChatMessage(new ChatComponentText("Invalid Arguments. Usage: /hitdelayfix <on/off>"));
            return;
        }

        HitDelayFix.INSTANCE.setEnabled(args[0].equalsIgnoreCase("on"));

        sender.addChatMessage(new ChatComponentText(String.format("HitDelayFix has been %s.", HitDelayFix.INSTANCE.isEnabled() ? "enabled" : "disabled")));
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