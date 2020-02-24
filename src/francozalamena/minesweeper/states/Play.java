package francozalamena.minesweeper.states;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import francozalamena.minesweeper.Game;
import francozalamena.minesweeper.model.Difficulty;
import francozalamena.minesweeper.model.Grid;
import francozalamena.minesweeper.model.tiles.BombTile;
import francozalamena.minesweeper.model.tiles.Tile;

public class Play extends BasicGameState {

	private int state;
	private Grid playField;
	
	public Play(int state) {
		this.state = state;
		try {
			this.playField = new Grid(10,10, 35, Difficulty.MEDIUM);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		for(int i = 0; i < playField.getTiles()[1].length; i++) {
			for(int j = 0; j<playField.getTiles()[0].length; j++) {
				Tile t = playField.getTiles()[i][j];
				t.setBombs(countBombs(i, j));
			}
		}
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.clear();
		playField.render(g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		if(Mouse.isButtonDown(Input.MOUSE_LEFT_BUTTON)) {
			for(int i = 0; i < playField.getTiles()[1].length; i++) {
				for(int j = 0; j<playField.getTiles()[0].length; j++) {
					Tile t = playField.getTile(i,j);
					if(mouseHitBounds(t)) {
						if(isBomb(t)) {
							((BombTile) t).explode();
							Game.gameOver();
						}else {
							t.onMouseClicked();
						}
					}
				}
			}
		}
	}
	
	private int countBombs(int row, int col) {
		int bombs = 0;
		for(int i= row - 1; i<=row+1; i++) {
			if(i > playField.getTiles()[0].length - 1 || i < 0) break;
			for(int j = col - 1; col<=col+1;j++){
				if(j > playField.getTiles()[1].length - 1 || j < 0) break;
				if(isBomb(playField.getTile(i,j)))
					bombs++;
			}
		}
		return bombs;
	}

	private boolean isBomb(Tile t) {
		return t instanceof BombTile;
	}	
	
	private boolean mouseHitBounds(Tile t) {
		return ((getMouseX() > t.getPosX()) && (getMouseX() < t.getPosX() + t.getSideSize()))
				&& ((getMouseY() > t.getPosY()) && (getMouseY() < t.getPosY() + t.getSideSize()));
	}

	@Override
	public int getID() {
		return state;
	}
	
	private double getMouseX() {
		return Mouse.getX();
	}
	
	private double getMouseY() {
		return Game.SCREEN_HEIGHT - Mouse.getY();
	}
}
