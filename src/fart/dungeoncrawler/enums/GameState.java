package fart.dungeoncrawler.enums;

/**
 * Describes all states the game can be in. This enum is used to easily switch the current GameState
 * inside the game-class. 
 * @author Felix
 *
 */
public enum GameState {
	InMenu,
	InGame,
	InConversation,
	InShop,
	InInventory,
	InLobby,
	InStatsMenu,
	InQuestLog
}
