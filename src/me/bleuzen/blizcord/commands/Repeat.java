package me.bleuzen.blizcord.commands;

import java.util.ArrayList;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import me.bleuzen.blizcord.PlayerThread;
import me.bleuzen.blizcord.Utils;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

class Repeat extends Command {

	@Override
	public String getName() {
		return "repeat";
	}

	@Override
	public void execute(String arg, User author, MessageChannel channel, Guild guild) {
		if(!Utils.isAdmin(author)) {
			channel.sendMessage(author.getAsMention() + " ``Sorry, only admins can use the repeat command.``").queue();
			return;
		}

		int repeats;
		if(arg == null) {
			repeats = 1;
		} else {
			try {
				repeats = Integer.parseInt(arg);
				if(repeats < 1) {
					throw new NumberFormatException();
				}
			} catch(NumberFormatException e) {
				channel.sendMessage(author.getAsMention() + " Invalid number").queue();
				return;
			}
		}

		if(PlayerThread.isPlaying()) {

			ArrayList<AudioTrack> songs = new ArrayList<>();
			songs.add(PlayerThread.getMusicManager().player.getPlayingTrack());
			ArrayList<AudioTrack> upcoming = PlayerThread.getMusicManager().scheduler.getList();
			if(!upcoming.isEmpty()) {
				for(int i = 0; i < upcoming.size(); i++) {
					songs.add(upcoming.get(i));
				}
			}

			for(int i = 0; i < repeats; i++) {
				for(int j = 0; j < songs.size(); j++) {
					PlayerThread.play(songs.get(j).makeClone());
				}
			}

			channel.sendMessage( "``Repeated the playlist" + (repeats == 1 ? ".``" : (" " + repeats + " times.``") )).queue();
		} else {
			channel.sendMessage(author.getAsMention() + " ``The playlist is empty. There is nothing to repeat.``").queue();
		}
	}

}