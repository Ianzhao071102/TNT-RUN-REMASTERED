/**
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */

package tntrun.signs.type;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.messages.Messages;
import tntrun.utils.FormattingCodesParser;
import tntrun.utils.Utils;

public class JoinSign implements SignType {

	private TNTRun plugin;

	public JoinSign(TNTRun plugin) {
		this.plugin = plugin;
	}

	@Override
	public void handleCreation(SignChangeEvent e) {
		String arenaname = ChatColor.stripColor(FormattingCodesParser.parseFormattingCodes(e.getLine(2)));
		final Arena arena = plugin.amanager.getArenaByName(arenaname);
		if (arena != null) {
			plugin.getSignEditor().createJoinSign(e.getBlock(), arenaname);
			Messages.sendMessage(e.getPlayer(), Messages.signcreate);
		} else {
			Messages.sendMessage(e.getPlayer(), Messages.arenanotexist.replace("{ARENA}", arenaname));
			e.setCancelled(true);
			e.getBlock().breakNaturally();
		}
	}

	@Override
	public void handleClick(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		Arena arena = plugin.amanager.getArenaByName(ChatColor.stripColor(((Sign) e.getClickedBlock().getState()).getLine(2)));
		if (arena == null) {
			Messages.sendMessage(player, Messages.arenanotexist);
			e.getClickedBlock().breakNaturally();
			return;
		}
		if (!arena.getStatusManager().isArenaRunning()) {
			if (arena.getPlayerHandler().checkJoin(player)) {
				arena.getPlayerHandler().spawnPlayer(player, Messages.playerjoinedtoothers);
				//attempt to cache the sign location as a fix for lost signinfo
				plugin.getSignEditor().addSign(e.getClickedBlock(), arena.getArenaName());
			}
			e.setCancelled(true);
		} else if (plugin.getConfig().getBoolean("signs.allowspectate") && arena.getPlayerHandler().canSpectate(player)) {
			arena.getPlayerHandler().spectatePlayer(player, Messages.playerjoinedasspectator, "");
			if (Utils.debug()) {
				plugin.getLogger().info("Player " + player.getName() + " joined arena " + arena.getArenaName() + " as a spectator");
			}
		}
	}

	@Override
	public void handleDestroy(BlockBreakEvent e) {
		Block b = e.getBlock();
		plugin.getSignEditor().removeSign(b, ChatColor.stripColor(((Sign) b.getState()).getLine(2)));
		Messages.sendMessage(e.getPlayer(), Messages.signremove);
	}

}
