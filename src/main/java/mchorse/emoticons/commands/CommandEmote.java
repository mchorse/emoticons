package mchorse.emoticons.commands;

import java.util.List;

import mchorse.emoticons.common.EmoteAPI;
import mchorse.emoticons.common.emotes.Emotes;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CommandEmote extends CommandBase
{
    @Override
    public String getName()
    {
        return "emote";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "emoticons.commands.emote.help";
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return true;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        String emote = args.length >= 1 ? args[0] : "";

        if (!Emotes.has(emote))
        {
            emote = "";
        }

        EmoteAPI.setEmoteClient(emote, Minecraft.getMinecraft().player);
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, Emotes.EMOTES.keySet());
        }

        return super.getTabCompletions(server, sender, args, targetPos);
    }
}