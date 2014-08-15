package org.spigotmc;

import com.google.common.base.Throwables;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import net.minecraft.util.gnu.trove.map.hash.TObjectIntHashMap;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class SpigotConfig
{

    private static final File CONFIG_FILE = new File( "spigot.yml" );
    private static final String HEADER = "This is the main configuration file for Spigot.\n"
            + "As you can see, there's tons to configure. Some options may impact gameplay, so use\n"
            + "with caution, and make sure you know what each option does before configuring.\n"
            + "For a reference for any variable inside this file, check out the Spigot wiki at\n"
            + "http://www.spigotmc.org/wiki/spigot-configuration/\n"
            + "\n"
            + "If you need help with the configuration or have any questions related to Spigot,\n"
            + "join us at the IRC or drop by our forums and leave a post.\n"
            + "\n"
            + "IRC: #spigot @ irc.esper.net ( http://webchat.esper.net/?channel=spigot )\n"
            + "Forums: http://www.spigotmc.org/forum/\n";
    /*========================================================================*/
    static YamlConfiguration config;
    static int version;
    static Map<String, Command> commands;
    /*========================================================================*/
    private static Metrics metrics;

    public static void init()
    {
        config = YamlConfiguration.loadConfiguration( CONFIG_FILE );
        config.options().header( HEADER );
        config.options().copyDefaults( true );

        commands = new HashMap<String, Command>();

        version = getInt( "config-version", 6 );
        set( "config-version", 6 );
        readConfig( SpigotConfig.class, null );
    }

    public static void registerCommands()
    {
        for ( Map.Entry<String, Command> entry : commands.entrySet() )
        {
            MinecraftServer.getServer().server.getCommandMap().register( entry.getKey(), "Spigot", entry.getValue() );
        }

        if ( metrics == null )
        {
            try
            {
                metrics = new Metrics();
                metrics.start();
            } catch ( IOException ex )
            {
                Bukkit.getServer().getLogger().log( Level.SEVERE, "Could not start metrics service", ex );
            }
        }
    }

    static void readConfig(Class<?> clazz, Object instance)
    {
        for ( Method method : clazz.getDeclaredMethods() )
        {
            if ( Modifier.isPrivate( method.getModifiers() ) )
            {
                if ( method.getParameterTypes().length == 0 && method.getReturnType() == Void.TYPE )
                {
                    try
                    {
                        method.setAccessible( true );
                        method.invoke( instance );
                    } catch ( InvocationTargetException ex )
                    {
                        Throwables.propagate( ex.getCause() );
                    } catch ( Exception ex )
                    {
                        Bukkit.getLogger().log( Level.SEVERE, "Error invoking " + method, ex );
                    }
                }
            }
        }

        try
        {
            config.save( CONFIG_FILE );
        } catch ( IOException ex )
        {
            Bukkit.getLogger().log( Level.SEVERE, "Could not save " + CONFIG_FILE, ex );
        }
    }

    private static void set(String path, Object val)
    {
        config.set( path, val );
    }

    private static boolean getBoolean(String path, boolean def)
    {
        config.addDefault( path, def );
        return config.getBoolean( path, config.getBoolean( path ) );
    }

    private static int getInt(String path, int def)
    {
        config.addDefault( path, def );
        return config.getInt( path, config.getInt( path ) );
    }

    private static <T> List getList(String path, T def)
    {
        config.addDefault( path, def );
        return (List<T>) config.getList( path, config.getList( path ) );
    }

    private static String getString(String path, String def)
    {
        config.addDefault( path, def );
        return config.getString( path, config.getString( path ) );
    }

    public static boolean logCommands;
    private static void logCommands()
    {
        logCommands = getBoolean( "commands.log", true );
    }

    public static int tabComplete;
    private static void tabComplete()
    {
        if ( version < 6 )
        {
            boolean oldValue = getBoolean( "commands.tab-complete", true );
            if ( oldValue )
            {
                set( "commands.tab-complete", 0 );
            } else
            {
                set( "commands.tab-complete", -1 );
            }
        }
        tabComplete = getInt( "commands.tab-complete", 0 );
    }

    public static String whitelistMessage;
    public static String unknownCommandMessage;
    public static String serverFullMessage;
    public static String outdatedClientMessage = "Outdated client! Please use {}";
    public static String outdatedServerMessage = "Outdated server! I\'m still on {0}";
    private static String transform(String s)
    {
        return ChatColor.translateAlternateColorCodes( '&', s ).replaceAll( "\\n", "\n" );
    }
    private static void messages()
    {
        if (version < 4)
        {
            set( "messages.outdated-client", outdatedClientMessage );
            set( "messages.outdated-server", outdatedServerMessage );
        }

        whitelistMessage = transform( getString( "messages.whitelist", "You are not whitelisted on this server!" ) );
        unknownCommandMessage = transform( getString( "messages.unknown-command", "Unknown command. Type \"/help\" for help." ) );
        serverFullMessage = transform( getString( "messages.server-full", "The server is full!" ) );
        outdatedClientMessage = transform( getString( "messages.outdated-client", outdatedClientMessage ) );
        outdatedServerMessage = transform( getString( "messages.outdated-server", outdatedServerMessage ) );
    }

    public static int timeoutTime = 60;
    public static boolean restartOnCrash = true;
    public static String restartScript = "./start.sh";
    public static String restartMessage;
    private static void watchdog()
    {
        timeoutTime = getInt( "settings.timeout-time", timeoutTime );
        restartOnCrash = getBoolean( "settings.restart-on-crash", restartOnCrash );
        restartScript = getString( "settings.restart-script", restartScript );
        restartMessage = transform( getString( "messages.restart", "Server is restarting" ) );
        commands.put( "restart", new RestartCommand( "restart" ) );
        WatchdogThread.doStart( timeoutTime, restartOnCrash );
    }

    public static boolean bungee;
    public static List<String> bungeeAddresses = Collections.singletonList("127.0.0.1");
    private static void bungee() {
        if ( version < 4 )
        {
            set( "settings.bungeecord", false );
            System.out.println( "Oudated config, disabling BungeeCord support!" );
        }
        bungee = getBoolean( "settings.bungeecord", false );
        bungeeAddresses = getList("settings.bungeecord-addresses", bungeeAddresses);
    }

    private static void nettyThreads()
    {
        int count = getInt( "settings.netty-threads", 4 );
        System.setProperty( "io.netty.eventLoopThreads", Integer.toString( count ) );
        Bukkit.getLogger().log( Level.INFO, "Using {0} threads for Netty based IO", count );
    }

    public static boolean lateBind;
    private static void lateBind() {
        lateBind = getBoolean( "settings.late-bind", false );
    }

    public static boolean disableStatSaving;
    public static TObjectIntHashMap<String> forcedStats = new TObjectIntHashMap<String>();
    private static void stats()
    {
        disableStatSaving = getBoolean( "stats.disable-saving", false );

        if ( !config.contains( "stats.forced-stats" ) ) {
            config.createSection( "stats.forced-stats" );
        }

        ConfigurationSection section = config.getConfigurationSection( "stats.forced-stats" );
        for ( String name : section.getKeys( true ) )
        {
            if ( section.isInt( name ) )
            {
                forcedStats.put( name, section.getInt( name ) );
            }
        }

        if ( disableStatSaving && section.getInt( "achievement.openInventory", 0 ) < 1 )
        {
            Bukkit.getLogger().warning( "*** WARNING *** stats.disable-saving is true but stats.forced-stats.achievement.openInventory" +
                    " isn't set to 1. Disabling stat saving without forcing the achievement may cause it to get stuck on the player's " +
                    "screen." );
        }
    }

    private static void tpsCommand()
    {
        commands.put( "tps", new TicksPerSecondCommand( "tps" ) );
    }

    public static int playerSample;
    private static void playerSample()
    {
        playerSample = getInt( "settings.sample-count", 12 );
        System.out.println( "Server Ping Player Sample Count: " + playerSample );
    }

    public static int playerShuffle;
    private static void playerShuffle()
    {
        playerShuffle = getInt( "settings.player-shuffle", 0 );
    }

    public static List<String> spamExclusions;
    private static void spamExclusions()
    {
        spamExclusions = getList( "commands.spam-exclusions", Arrays.asList( new String[]
        {
                "/skill"
        } ) );
    }

    public static boolean silentCommandBlocks;
    private static void silentCommandBlocks()
    {
        silentCommandBlocks = getBoolean( "commands.silent-commandblock-console", false );
    }

    public static boolean filterCreativeItems;
    private static void filterCreativeItems()
    {
        filterCreativeItems = getBoolean( "settings.filter-creative-items", true );
    }

    public static Set<String> replaceCommands;
    private static void replaceCommands()
    {
        if ( config.contains( "replace-commands" ) )
        {
            set( "commands.replace-commands", config.getStringList( "replace-commands" ) );
            config.set( "replace-commands", null );
        }
        replaceCommands = new HashSet<String>( (List<String>) getList( "commands.replace-commands",
                Arrays.asList( "setblock", "summon", "testforblock", "tellraw" ) ) );
    }
    
    public static int userCacheCap;
    private static void userCacheCap()
    {
        userCacheCap = getInt( "settings.user-cache-size", 1000 );
    }
    
    public static boolean saveUserCacheOnStopOnly;
    private static void saveUserCacheOnStopOnly()
    {
        saveUserCacheOnStopOnly = getBoolean( "settings.save-user-cache-on-stop-only", false );
    }

    public static int intCacheLimit;
    private static void intCacheLimit()
    {
        intCacheLimit = getInt( "settings.int-cache-limit", 1024 );
    }
}