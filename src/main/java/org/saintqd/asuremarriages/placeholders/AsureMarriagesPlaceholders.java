package org.saintqd.asuremarriages.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.saintqd.asuremarriages.AsureMarriages;

public class AsureMarriagesPlaceholders extends PlaceholderExpansion {

    private final AsureMarriages plugin;

    public AsureMarriagesPlaceholders(AsureMarriages plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean persist(){
        return true;
    }

    @Override
    public boolean canRegister(){
        return true;
    }

    @Override
    public @NotNull String getAuthor(){
        return plugin.getPluginMeta().getAuthors().toString();
    }

    @Override
    public @NotNull String getIdentifier(){
        return "asuremarriages";
    }

    @Override
    public @NotNull String getVersion(){
        return plugin.getPluginMeta().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier){

        if(player == null){
            return "";
        }

        return Placeholder.valueOf(identifier.toUpperCase()).placeholderResult(plugin,player);
    }

    public enum Placeholder {

        MARRY_NAME {
            @Override
            public String placeholderResult(AsureMarriages plugin, Player player) {
                String partnerName = plugin.getMarriedPlayersManager().getMarriedPlayerNames().get(player.getName());
                if (partnerName != null)
                    return partnerName;
                return "-";
            }
        };

        public abstract String placeholderResult(AsureMarriages plugin, Player player);
    }
}
