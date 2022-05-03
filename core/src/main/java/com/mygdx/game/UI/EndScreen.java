package com.mygdx.game.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.Components.Pirate;
import com.mygdx.game.Entitys.Player;
import com.mygdx.game.Managers.GameManager;
import com.mygdx.game.Managers.ResourceManager;
import com.mygdx.game.PirateGame;

import static com.mygdx.utils.Constants.VIEWPORT_HEIGHT;

/**
 * Contains widgets defining the game end screen.
 */
public class EndScreen extends Page {
    Label wonText;
    Label playerStats;

    public EndScreen(PirateGame game) {
        super(game);
    }

    /**
     * Set game end screen status to report a win.
     */
    public void win() {
        wonText.setText("Congrats You Have Won");
    }

    /**
     * Create game end screen widgets, initialised to game loss status.
     */
    @Override
    protected void CreateActors() {
        Table t = new Table();
        t.setBackground(new TextureRegionDrawable(ResourceManager.getTexture("menuBG.jpg")));

        float space = VIEWPORT_HEIGHT * 0.25f;
        t.setFillParent(true);
        actors.add(t);
        wonText = new Label("You have lost", parent.skin);
        wonText.setFontScale(2);
        t.top();
        t.add(wonText).top().spaceBottom(space);
        t.row();
        playerStats = new Label("Player Stats:\n", parent.skin);
        t.add(playerStats).spaceBottom(space);
        t.row();
        TextButton b = new TextButton("Exit", parent.skin);
        b.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
                System.exit(0);
            }
        });
        t.add(b).size(100, 25).top().spaceBottom(space* 0.5f);
        //Ayman - restart button
        t.row();
        TextButton r = new TextButton("Restart", parent.skin);
        r.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Restart");
                //Ayman - restart game
                GameManager.restartGame();
                parent.setScreen(parent.game);
            }
        });
        t.add(r);
    }

    @Override
    protected void update() {
        super.update();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
            System.exit(0);
        }
    }

    /**
     * Get player stats such as plunder etc. and display game end screen.
     */
    @Override
    public void show() {
        super.show();
        Player p = GameManager.getPlayer();
        //Ayman - added display of points and time
        int time = (int) (GameManager.getPlayer().getComponent(Pirate.class).getTime());
        int totalPoint = (p.getComponent(Pirate.class).getPoints()) - (time/2);
        int p1 = time % 60;
        int p2 = time / 60;
        int p3 = p2 % 60;
        p2 = p2 / 60;
        String stats = String.format("Health: %s\nAmmo: %s\nPlunder: %s\nPoints: %s\nTime: %s", p.getHealth(), p.getAmmo(), p.getPlunder(),fixPoint(totalPoint), p2 + ":" + p3 + ":" + p1);
        playerStats.setText(stats);
    }

    //Ayman - function to ensure positive points always:
    public int fixPoint(int p) {
        if (p <= 0) {
            return 0;
        } else {
            return p;
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        Table t = (Table) actors.get(0);
        t.setBackground(new TextureRegionDrawable(ResourceManager.getTexture("menuBG.jpg"))); // prevent the bg being stretched
    }
}
