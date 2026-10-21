using System;
using System.IO;
using UnityEngine;

namespace Wrecked
{
    /// <summary>
    /// Translates Data.bb's binary file save/load into JSON files under
    /// Application.persistentDataPath.  Slot 0 = "Default" (deathmatch scratch),
    /// slots 1-3 = save files.
    /// </summary>
    public class SaveSystem : MonoBehaviour
    {
        // ── Option data (flat, no slot) ────────────────────────────────────────
        [Serializable]
        private class OptionData
        {
            public int  music   = 1;
            public int  atmos   = 1;
            public int  fx      = 1;
            public int  blood   = 1;
            // Key bindings (KeyCode ints)
            public int  keyAttackA  = (int)KeyCode.Z;
            public int  keyAttackB  = (int)KeyCode.X;
            public int  keyPickA    = (int)KeyCode.A;
            public int  keyPickB    = (int)KeyCode.S;
            public int  keyJump     = (int)KeyCode.Space;
            public int  keySleep    = (int)KeyCode.Return;
        }

        // ── Progress data (3 slots) ────────────────────────────────────────────
        [Serializable]
        private class ProgressData
        {
            public int[]   gamTime     = new int[4];
            public int[]   gamDay      = new int[4];
            public int[]   gamSoul     = new int[4];
            public int[,]  gamSins     = new int[4, 21];
            public int[]   gamCrime    = new int[4];
            public int[]   gamMission  = new int[4];
            public int[]   gamMissionDay = new int[4];
            public int[,]  gamResult   = new int[4, 21];
            public int[,]  gamCheckpoint = new int[4, 11];
        }

        // ── World data ─────────────────────────────────────────────────────────
        [Serializable]
        private class WorldData
        {
            public int    camType;
            public float  camX, camY, camZ;
            public float  camTX, camTY, camTZ;
            // Atmosphere colours (0-255 int)
            public float  ambR, ambG, ambB;
            public float  lightR, lightG, lightB;
            public float  atmosR, atmosG, atmosB;
            public float  skyR, skyG, skyB;
            // Fires (5 slots)
            public int[]   fireState  = new int[6];
            public int[]   fireSource = new int[6];
            public float[] fireX      = new float[6];
            public float[] fireY      = new float[6];
            public float[] fireZ      = new float[6];
            public int[]   fireTim    = new int[6];
            // Boats (5 slots)
            public int[]   boatState  = new int[6];
            public float[] boatX      = new float[6];
            public float[] boatY      = new float[6];
            public float[] boatZ      = new float[6];
            public float[] boatA      = new float[6];
        }

        // ── Character data ─────────────────────────────────────────────────────
        [Serializable]
        private class CharacterSaveData
        {
            public int[]   pIsland   = new int[GameConstants.MAX_PLAYERS + 1];
            public float[] pX        = new float[GameConstants.MAX_PLAYERS + 1];
            public float[] pY        = new float[GameConstants.MAX_PLAYERS + 1];
            public float[] pZ        = new float[GameConstants.MAX_PLAYERS + 1];
            public float[] pA        = new float[GameConstants.MAX_PLAYERS + 1];
            public float[] pGravity  = new float[GameConstants.MAX_PLAYERS + 1];
            public int[]   pAnim     = new int[GameConstants.MAX_PLAYERS + 1];
            public int[]   pAgenda   = new int[GameConstants.MAX_PLAYERS + 1];
            public int[]   pTarget   = new int[GameConstants.MAX_PLAYERS + 1];
            public float[] pTX       = new float[GameConstants.MAX_PLAYERS + 1];
            public float[] pTZ       = new float[GameConstants.MAX_PLAYERS + 1];
            public float[] pTA       = new float[GameConstants.MAX_PLAYERS + 1];
            public int[]   pType     = new int[GameConstants.MAX_PLAYERS + 1];
            public int[]   pState    = new int[GameConstants.MAX_PLAYERS + 1];
            public int[]   pHunger   = new int[GameConstants.MAX_PLAYERS + 1];
            public int[]   pThirst   = new int[GameConstants.MAX_PLAYERS + 1];
            public int[]   pHealth   = new int[GameConstants.MAX_PLAYERS + 1];
            public int[]   pHP       = new int[GameConstants.MAX_PLAYERS + 1];
            public int[]   pDT       = new int[GameConstants.MAX_PLAYERS + 1];
            public int[]   pCarryA   = new int[GameConstants.MAX_PLAYERS + 1];
            public int[]   pCarryB   = new int[GameConstants.MAX_PLAYERS + 1];
            public int[]   pCostume  = new int[GameConstants.MAX_PLAYERS + 1];
            public int[]   pHair     = new int[GameConstants.MAX_PLAYERS + 1];
            public int[]   pFace     = new int[GameConstants.MAX_PLAYERS + 1];
            public int[]   pBody     = new int[GameConstants.MAX_PLAYERS + 1];
            public int[]   pArms     = new int[GameConstants.MAX_PLAYERS + 1];
            public int[]   pLegs     = new int[GameConstants.MAX_PLAYERS + 1];
            // pScar[MAX_PLAYERS+1, 21] flattened
            public int[]   pScarFlat = new int[(GameConstants.MAX_PLAYERS + 1) * 21];
        }

        // ── Resource data ──────────────────────────────────────────────────────
        [Serializable]
        private class ResourceSaveData
        {
            public int[]   resIsland = new int[GameConstants.MAX_RESOURCES + 1];
            public int[]   resType   = new int[GameConstants.MAX_RESOURCES + 1];
            public int[]   resState  = new int[GameConstants.MAX_RESOURCES + 1];
            public float[] resX      = new float[GameConstants.MAX_RESOURCES + 1];
            public float[] resY      = new float[GameConstants.MAX_RESOURCES + 1];
            public float[] resZ      = new float[GameConstants.MAX_RESOURCES + 1];
            public float[] resA      = new float[GameConstants.MAX_RESOURCES + 1];
            public int[]   resHP     = new int[GameConstants.MAX_RESOURCES + 1];
        }

        // ── Item data ──────────────────────────────────────────────────────────
        [Serializable]
        private class ItemSaveData
        {
            public int[]   iIsland   = new int[GameConstants.MAX_ITEMS + 1];
            public int[]   iType     = new int[GameConstants.MAX_ITEMS + 1];
            public int[]   iState    = new int[GameConstants.MAX_ITEMS + 1];
            public float[] iX        = new float[GameConstants.MAX_ITEMS + 1];
            public float[] iY        = new float[GameConstants.MAX_ITEMS + 1];
            public float[] iZ        = new float[GameConstants.MAX_ITEMS + 1];
            public float[] iA        = new float[GameConstants.MAX_ITEMS + 1];
            public int[]   iFlaming  = new int[GameConstants.MAX_ITEMS + 1];
        }

        // ══════════════════════════════════════════════════════════════════════

        private string RootPath => Application.persistentDataPath;
        private string SlotPath(int slot) => slot > 0
            ? Path.Combine(RootPath, $"Save0{slot}")
            : Path.Combine(RootPath, "Default");

        private void EnsureDir(string path)
        {
            if (!Directory.Exists(path)) Directory.CreateDirectory(path);
        }

        private void WriteJson<T>(string path, T data)
        {
            EnsureDir(Path.GetDirectoryName(path));
            File.WriteAllText(path, JsonUtility.ToJson(data, true));
        }

        private T ReadJson<T>(string path) where T : new()
        {
            if (!File.Exists(path)) return new T();
            try   { return JsonUtility.FromJson<T>(File.ReadAllText(path)); }
            catch { return new T(); }
        }

        // ══════════════════════════════════════════════════════════════════════
        //  Public API
        // ══════════════════════════════════════════════════════════════════════

        public void SaveAll()
        {
            var gm = GameManager.Instance;
            int slot = gm.SaveSlot;
            SaveProgressData();
            SaveWorldData(slot);
            SaveCharacterData(slot);
            SaveResourceData(slot);
            SaveItemData(slot);
        }

        // ── Options ────────────────────────────────────────────────────────────

        public void SaveOptionData(int music, int atmos, int fx, int blood,
            KeyCode kAtA, KeyCode kAtB, KeyCode kPkA, KeyCode kPkB,
            KeyCode kJmp, KeyCode kSlp)
        {
            var d = new OptionData
            {
                music = music, atmos = atmos, fx = fx, blood = blood,
                keyAttackA = (int)kAtA, keyAttackB = (int)kAtB,
                keyPickA   = (int)kPkA, keyPickB   = (int)kPkB,
                keyJump    = (int)kJmp, keySleep   = (int)kSlp
            };
            WriteJson(Path.Combine(RootPath, "Options.json"), d);
        }

        public OptionData LoadOptionData() =>
            ReadJson<OptionData>(Path.Combine(RootPath, "Options.json"));

        // ── Progress ───────────────────────────────────────────────────────────

        public void SaveProgressData()
        {
            var gm = GameManager.Instance;
            var d = new ProgressData();
            for (int slot = 0; slot <= 3; slot++)
            {
                d.gamTime[slot]     = gm.GameTime[slot];
                d.gamDay[slot]      = gm.GameDay[slot];
                d.gamSoul[slot]     = gm.GameSoul[slot];
                d.gamCrime[slot]    = gm.GameCrime[slot];
                d.gamMission[slot]  = gm.GameMission[slot];
                d.gamMissionDay[slot] = gm.MissionDay[slot];
                for (int i = 1; i <= 20; i++)
                {
                    d.gamSins[slot, i]    = gm.GameSins[slot, i];
                    d.gamResult[slot, i]  = gm.GameResult[slot, i];
                }
                for (int i = 1; i <= 10; i++)
                    d.gamCheckpoint[slot, i] = gm.Checkpoint[slot, i];
            }
            WriteJson(Path.Combine(RootPath, "Progress.json"), d);
        }

        public void LoadProgressData()
        {
            var gm = GameManager.Instance;
            var d  = ReadJson<ProgressData>(Path.Combine(RootPath, "Progress.json"));
            for (int slot = 0; slot <= 3; slot++)
            {
                gm.GameTime[slot]     = d.gamTime[slot];
                gm.GameDay[slot]      = d.gamDay[slot];
                gm.GameSoul[slot]     = d.gamSoul[slot];
                gm.GameCrime[slot]    = d.gamCrime[slot];
                gm.GameMission[slot]  = d.gamMission[slot];
                gm.MissionDay[slot]   = d.gamMissionDay[slot];
                for (int i = 1; i <= 20; i++)
                {
                    gm.GameSins[slot, i]   = d.gamSins[slot, i];
                    gm.GameResult[slot, i] = d.gamResult[slot, i];
                }
                for (int i = 1; i <= 10; i++)
                    gm.Checkpoint[slot, i] = d.gamCheckpoint[slot, i];
            }
        }

        // ── World ──────────────────────────────────────────────────────────────

        public void SaveWorldData(int slot)
        {
            var atm = GameManager.Instance.AtmosMgr;
            var cam = GameManager.Instance.CamCtrl;
            var fire = GameManager.Instance.FireSys;
            var boat = GameManager.Instance.BoatSys;

            var d = new WorldData();
            d.camType = cam.CamType;
            var cp = cam.CamPosition; d.camX = cp.x; d.camY = cp.y; d.camZ = cp.z;
            var ct = cam.CamTarget;   d.camTX = ct.x; d.camTY = ct.y; d.camTZ = ct.z;

            var ac = atm.AmbientColor;
            d.ambR = ac.r; d.ambG = ac.g; d.ambB = ac.b;
            var lc = atm.LightColor;
            d.lightR = lc.r; d.lightG = lc.g; d.lightB = lc.b;
            var fc = atm.FogColor;
            d.atmosR = fc.r; d.atmosG = fc.g; d.atmosB = fc.b;
            var sc = atm.SkyColor;
            d.skyR = sc.r; d.skyG = sc.g; d.skyB = sc.b;

            for (int i = 1; i <= 5; i++)
            {
                d.fireState[i]  = fire.GetState(i);
                d.fireSource[i] = fire.GetSource(i);
                var fp = fire.GetPosition(i);
                d.fireX[i] = fp.x; d.fireY[i] = fp.y; d.fireZ[i] = fp.z;
                d.fireTim[i]    = fire.GetTimer(i);
                d.boatState[i]  = boat.GetState(i);
                var bp = boat.GetPosition(i);
                d.boatX[i] = bp.x; d.boatY[i] = bp.y; d.boatZ[i] = bp.z;
                d.boatA[i]      = boat.GetAngle(i);
            }
            WriteJson(Path.Combine(SlotPath(slot), "World.json"), d);
        }

        public void LoadWorldData(int slot)
        {
            var d = ReadJson<WorldData>(Path.Combine(SlotPath(slot), "World.json"));
            var atm  = GameManager.Instance.AtmosMgr;
            var cam  = GameManager.Instance.CamCtrl;
            var fire = GameManager.Instance.FireSys;
            var boat = GameManager.Instance.BoatSys;

            cam.CamType     = d.camType;
            cam.CamPosition = new UnityEngine.Vector3(d.camX, d.camY, d.camZ);
            cam.CamTarget   = new UnityEngine.Vector3(d.camTX, d.camTY, d.camTZ);

            atm.AmbientColor = new UnityEngine.Color(d.ambR, d.ambG, d.ambB);
            atm.LightColor   = new UnityEngine.Color(d.lightR, d.lightG, d.lightB);
            atm.FogColor     = new UnityEngine.Color(d.atmosR, d.atmosG, d.atmosB);
            atm.SkyColor     = new UnityEngine.Color(d.skyR, d.skyG, d.skyB);

            for (int i = 1; i <= 5; i++)
            {
                fire.Restore(i, d.fireState[i], d.fireSource[i],
                    new UnityEngine.Vector3(d.fireX[i], d.fireY[i], d.fireZ[i]),
                    d.fireTim[i]);
                boat.Restore(i, d.boatState[i],
                    new UnityEngine.Vector3(d.boatX[i], d.boatY[i], d.boatZ[i]),
                    d.boatA[i]);
            }
        }

        // ── Characters ─────────────────────────────────────────────────────────

        public void SaveCharacterData(int slot)
        {
            var mgr = GameManager.Instance.CharMgr;
            var d = new CharacterSaveData();
            for (int i = 1; i <= GameConstants.MAX_PLAYERS; i++)
            {
                var c = mgr.Get(i);
                d.pIsland[i] = c.Island; d.pX[i] = c.Pos.x; d.pY[i] = c.Pos.y; d.pZ[i] = c.Pos.z;
                d.pA[i] = c.Angle; d.pGravity[i] = c.Gravity; d.pAnim[i] = c.Anim;
                d.pAgenda[i] = c.Agenda; d.pTarget[i] = c.Target;
                d.pTX[i] = c.TargetPos.x; d.pTZ[i] = c.TargetPos.z; d.pTA[i] = c.TargetAngle;
                d.pType[i] = c.CharType; d.pState[i] = c.State;
                d.pHunger[i] = c.Hunger; d.pThirst[i] = c.Thirst;
                d.pHealth[i] = c.Health; d.pHP[i] = c.MaxHP; d.pDT[i] = c.DT;
                d.pCarryA[i] = c.CarryA; d.pCarryB[i] = c.CarryB;
                d.pCostume[i] = c.Costume; d.pHair[i] = c.Hair;
                d.pFace[i] = c.Face; d.pBody[i] = c.Body;
                d.pArms[i] = c.Arms; d.pLegs[i] = c.Legs;
                for (int lm = 1; lm <= 20; lm++)
                    d.pScarFlat[i * 21 + lm] = c.GetScar(lm);
            }
            WriteJson(Path.Combine(SlotPath(slot), "Characters.json"), d);
        }

        public void LoadCharacterData(int slot)
        {
            var d   = ReadJson<CharacterSaveData>(Path.Combine(SlotPath(slot), "Characters.json"));
            var mgr = GameManager.Instance.CharMgr;
            for (int i = 1; i <= GameConstants.MAX_PLAYERS; i++)
            {
                var c = mgr.Get(i);
                c.Island  = d.pIsland[i];
                c.Pos     = new UnityEngine.Vector3(d.pX[i], d.pY[i], d.pZ[i]);
                c.Angle   = d.pA[i]; c.Gravity = d.pGravity[i]; c.Anim = d.pAnim[i];
                c.Agenda  = d.pAgenda[i]; c.Target = d.pTarget[i];
                c.TargetPos   = new UnityEngine.Vector3(d.pTX[i], 0f, d.pTZ[i]);
                c.TargetAngle = d.pTA[i];
                c.CharType = d.pType[i]; c.State = d.pState[i];
                c.Hunger   = d.pHunger[i]; c.Thirst = d.pThirst[i];
                c.Health   = d.pHealth[i]; c.MaxHP  = d.pHP[i]; c.DT = d.pDT[i];
                c.CarryA   = d.pCarryA[i]; c.CarryB = d.pCarryB[i];
                c.Costume  = d.pCostume[i]; c.Hair = d.pHair[i];
                c.Face     = d.pFace[i]; c.Body = d.pBody[i];
                c.Arms     = d.pArms[i]; c.Legs = d.pLegs[i];
                for (int lm = 1; lm <= 20; lm++)
                    c.SetScar(lm, d.pScarFlat[i * 21 + lm]);
            }
        }

        // ── Resources ──────────────────────────────────────────────────────────

        public void SaveResourceData(int slot)
        {
            var mgr = GameManager.Instance.ResMgr;
            var d   = new ResourceSaveData();
            for (int i = 1; i <= GameConstants.MAX_RESOURCES; i++)
            {
                var r = mgr.Get(i);
                d.resIsland[i] = r.Island; d.resType[i] = r.ResType;
                d.resState[i]  = r.State;
                d.resX[i] = r.Pos.x; d.resY[i] = r.Pos.y; d.resZ[i] = r.Pos.z;
                d.resA[i] = r.Angle; d.resHP[i] = r.HP;
            }
            WriteJson(Path.Combine(SlotPath(slot), "Resources.json"), d);
        }

        public void LoadResourceData(int slot)
        {
            var d   = ReadJson<ResourceSaveData>(Path.Combine(SlotPath(slot), "Resources.json"));
            var mgr = GameManager.Instance.ResMgr;
            for (int i = 1; i <= GameConstants.MAX_RESOURCES; i++)
            {
                var r = mgr.Get(i);
                r.Island = d.resIsland[i]; r.ResType = d.resType[i];
                r.State  = d.resState[i];
                r.Pos    = new UnityEngine.Vector3(d.resX[i], d.resY[i], d.resZ[i]);
                r.Angle  = d.resA[i]; r.HP = d.resHP[i];
            }
        }

        // ── Items ──────────────────────────────────────────────────────────────

        public void SaveItemData(int slot)
        {
            var mgr = GameManager.Instance.ItemMgr;
            var d   = new ItemSaveData();
            for (int i = 1; i <= GameConstants.MAX_ITEMS; i++)
            {
                var it = mgr.Get(i);
                d.iIsland[i] = it.Island; d.iType[i] = it.ItemType;
                d.iState[i]  = it.State;
                d.iX[i] = it.Pos.x; d.iY[i] = it.Pos.y; d.iZ[i] = it.Pos.z;
                d.iA[i] = it.Angle; d.iFlaming[i] = it.Flaming ? 1 : 0;
            }
            WriteJson(Path.Combine(SlotPath(slot), "Items.json"), d);
        }

        public void LoadItemData(int slot)
        {
            var d   = ReadJson<ItemSaveData>(Path.Combine(SlotPath(slot), "Items.json"));
            var mgr = GameManager.Instance.ItemMgr;
            for (int i = 1; i <= GameConstants.MAX_ITEMS; i++)
            {
                var it = mgr.Get(i);
                it.Island   = d.iIsland[i]; it.ItemType = d.iType[i];
                it.State    = d.iState[i];
                it.Pos      = new UnityEngine.Vector3(d.iX[i], d.iY[i], d.iZ[i]);
                it.Angle    = d.iA[i]; it.Flaming = d.iFlaming[i] > 0;
            }
        }
    }
}
