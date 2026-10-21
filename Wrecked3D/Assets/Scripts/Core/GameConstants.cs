using UnityEngine;

namespace Wrecked
{
    public static class GameConstants
    {
        public const int MAX_PLAYERS      = 60;
        public const int MAX_RESOURCES    = 150;
        public const int MAX_ITEMS        = 375;
        public const int MAX_PARTICLES    = 400;
        public const int MAX_FIRES        = 5;
        public const int MAX_BOATS        = 5;
        public const int ITEMS_PER_ISLAND = 75;
        public const int FPS_TARGET       = 30;

        // Day/night time thresholds (gamTime max = 18000)
        public const int DAWN_START       = 0;
        public const int DAWN_END         = 3000;
        public const int DAY_START        = 3000;
        public const int DAY_END          = 6000;
        public const int DUSK_START       = 6000;
        public const int DUSK_END         = 9000;
        public const int TWILIGHT_START   = 9000;
        public const int TWILIGHT_END     = 12000;
        public const int NIGHT_START      = 12000;
        public const int NIGHT_END        = 15000;
        public const int TWILIGHT2_START  = 15000;
        public const int TWILIGHT2_END    = 18000;
        public const int DAY_LENGTH       = 18000;
        public const int SLEEP_TIME_START = 11000;
        public const int SLEEP_TIME_END   = 16000;

        public const float SEA_LEVEL      = 1.5f;

        // Game modes
        public const int MODE_DEATHMATCH  = 0;
        public const int MODE_STORY       = 1;
        public const int MODE_ENDING      = 2;

        // AI agendas
        public const int AGENDA_EXPLORE   = 0;
        public const int AGENDA_ATTACK    = 1;
        public const int AGENDA_RESOURCE  = 2;
        public const int AGENDA_GATHER    = 3;

        // Soul values
        public const int SOUL_EVIL        = 1;
        public const int SOUL_FLAWED      = 2;
        public const int SOUL_DIVINE      = 3;

        // Island IDs
        public const int ISLAND_NORTH     = 1;
        public const int ISLAND_EAST      = 2;
        public const int ISLAND_SOUTH     = 3;
        public const int ISLAND_WEST      = 4;
        public const int ISLAND_CENTRE    = 5;

        // Character type IDs
        public const int TYPE_PLAYER      = 1;
        public const int TYPE_SOLDIER     = 2;
        public const int TYPE_MYSTIC      = 3;
        public const int TYPE_NATIVE      = 4;
        public const int TYPE_LEOPARD     = 10;
        public const int TYPE_WOLF        = 11;
        public const int TYPE_DEER        = 12;
        public const int TYPE_RABBIT      = 13;
        public const int TYPE_FISH        = 20;

        // Item type IDs (resources)
        public const int ITEM_VINES       = 1;
        public const int ITEM_WOOD        = 2;
        public const int ITEM_STONE       = 3;
        public const int ITEM_MEAT        = 4;
        public const int ITEM_FRUIT       = 5;
        // Item type IDs (weapons/tools)
        public const int ITEM_WHIP        = 10;
        public const int ITEM_STICK       = 11;
        public const int ITEM_ROCK        = 12;
        public const int ITEM_HAMMER      = 13;
        public const int ITEM_SPEAR       = 14;
        public const int ITEM_AXE         = 15;
        public const int ITEM_GUN         = 16;
        public const int ITEM_ARTEFACT    = 17;
        public const int ITEM_BONE        = 18;
        public const int ITEM_TRAP        = 19;
        public const int ITEM_OAR         = 20;

        // Resource type IDs
        public const int RES_BARE_TREE    = 1;
        public const int RES_PALM_TREE    = 2;
        public const int RES_TREE         = 3;
        public const int RES_FRUIT_TREE   = 4;
        public const int RES_BUSH         = 5;
        public const int RES_FRUIT_BUSH   = 6;
        public const int RES_PLANT        = 7;
        public const int RES_ROCK         = 8;

        // Attack aim states
        public const int AIM_HIGH         = 1;
        public const int AIM_LOW          = 2;
        public const int AIM_GROUND       = 3;

        // Animation IDs (matching original sequences)
        public const int ANIM_IDLE        = 0;
        public const int ANIM_WALK        = 1;
        public const int ANIM_JUMP        = 2;
        public const int ANIM_FALL        = 3;
        public const int ANIM_BOAT        = 5;
        public const int ANIM_PICK_LEFT   = 10;
        public const int ANIM_PICK_RIGHT  = 11;
        public const int ANIM_DROP_LEFT   = 12;
        public const int ANIM_DROP_RIGHT  = 13;
        public const int ANIM_THROW_LEFT  = 14;
        public const int ANIM_THROW_RIGHT = 15;
        public const int ANIM_ATK_HIGH_L  = 20;
        public const int ANIM_ATK_HIGH_R  = 21;
        public const int ANIM_ATK_LOW_L   = 22;
        public const int ANIM_ATK_LOW_R   = 23;
        public const int ANIM_ATK_GRD_L   = 24;
        public const int ANIM_ATK_GRD_R   = 25;
        public const int ANIM_ATK_FLY_L   = 26;
        public const int ANIM_ATK_FLY_R   = 27;
        public const int ANIM_PAIN_HIGH   = 30;
        public const int ANIM_PAIN_LOW    = 31;
        public const int ANIM_COLLAPSE    = 32;
        public const int ANIM_DEAD        = 33;
        public const int ANIM_DEAD_GRD    = 34;
        public const int ANIM_SLEEP       = 36;
        public const int ANIM_SLEEP_LOOP  = 37;
        public const int ANIM_DEATH_FLY   = 39;
    }
}
