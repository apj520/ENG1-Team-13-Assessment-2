package com.mygdx.game.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.Managers.ResourceManager;
import com.mygdx.game.PirateGame;

import static com.mygdx.game.PirateGame.prefs;
import static com.mygdx.utils.Constants.VIEWPORT_HEIGHT;

/**
 * Contains widgets defining the start-of-game menu screen.
 */
public class MenuScreen extends Page {
    public MenuScreen(PirateGame parent) {
        super(parent);
    }

    /**
     * Create menu widgets such as start button, labels, etc.
     */
    @Override
    protected void CreateActors() {
        Table t = new Table();
        t.setFillParent(true);

        float space = VIEWPORT_HEIGHT * 0.15f;

        t.setBackground(new TextureRegionDrawable(ResourceManager.getTexture("menuBG.jpg")));
        Label l = new Label("Pirates the movie the game", parent.skin);
        l.setFontScale(2);
        t.add(l).top().spaceBottom(space * 0.5f);
        t.row();


        //AYMAN DIFF CHANGE:
        TextButton play = new TextButton("Easy", parent.skin);
        play.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //set difficulty to easy
                prefs.putString("difficulty", "GameSettingsEasy.json");
                //parent.setScreen(parent.game);
                System.out.println(prefs.getString("difficulty"));
                parent.startGame(parent.id_map);
                //GameManager.setDifficulty(prefs.getString("difficulty"));
                //int mid_health = 80;
                //GameManager.getSettings().get("starting").get("health").setType(mid_health);
            }
        });
        t.add(play).top().size(100, 25).spaceBottom(space);
        t.row();
        //AYMAN CHANGE:
        TextButton mid = new TextButton("Medium", parent.skin);
        mid.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //set difficulty to easy
                prefs.putString("difficulty", "GameSettingsMedium.json");
                //parent.setScreen(parent.game);
                System.out.println(prefs.getString("difficulty"));
                parent.startGame(parent.id_map);
                //GameManager.getSettings().
                //GameManager.setDifficulty(prefs.getString("difficulty"));
            }
        });
        t.add(mid).top().size(100, 25).spaceBottom(space);
        t.row();
        //HARD
        TextButton hard = new TextButton("Hard", parent.skin);
        hard.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //set difficulty to easy
                prefs.putString("difficulty", "GameSettingsHard.json");
                //parent.setScreen(parent.game);
                System.out.println(prefs.getString("difficulty"));
                parent.startGame(parent.id_map);
            }
        });
        t.add(hard).top().size(100, 25).spaceBottom(space);
        t.row();
        //AYMAN SAVE CHANGE: Load save file
        TextButton contd = new TextButton("Continue", parent.skin);
        contd.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("load save");
                //AYMAN SAVE CHANGE
                prefs.putString("difficulty", "GameSettingsSaved.json");
                System.out.println(prefs.getString("difficulty"));
                parent.startGame(parent.id_map);
                //CHANGE END
            }
        });
        t.add(contd).top().size(100, 25).spaceBottom(space);
        t.row();
        //CHANGE END
        TextButton quit = new TextButton("Quit", parent.skin);
        quit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
                System.exit(0);
            }
        });
        t.add(quit).size(100, 25).top().spaceBottom(space);

        t.top();

        actors.add(t);
    }

    @Override
    public void show() {
        super.show();
    }


    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        Table t = (Table) actors.get(0);
        t.setBackground(new TextureRegionDrawable(ResourceManager.getTexture("menuBG.jpg"))); // prevent the bg being stretched
    }
}
