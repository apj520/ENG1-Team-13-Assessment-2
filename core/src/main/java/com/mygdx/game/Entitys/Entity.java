package com.mygdx.game.Entitys;

import com.mygdx.game.Components.Component;
import com.mygdx.game.Components.ComponentEvent;
import com.mygdx.game.Components.ComponentType;
import com.mygdx.game.Managers.EntityManager;

import java.util.ArrayList;

/**
 * The base class for all entities in the game.
 * I am calling an entity pretty much anything that the user sees or interacts with except the UI.
 * However, there is over head with this class so in some cases it's better to just use raw sprites
 */
public class Entity {
    private static int entityCount = 0;
    private String entityName;
    private final ArrayList<Component> components;

    public Entity() {
        components = new ArrayList<>();
        entityName = "Entity (" + ++entityCount + ")"; // makes ure by default every entity has a unique name (although it's not automatically important if they don't)
        EntityManager.addEntity(this);
    }

    /**
     * Allocates the correct amount of memory for components
     *
     * @param numComponents number of components to allocate memory for
     */
    public Entity(int numComponents) {
        this();
        components.ensureCapacity(numComponents);
    }

    public final void setName(String name) {
        EntityManager.changeName(entityName, name);
        entityName = name;
    }

    public final String getName() {
        return entityName;
    }

    public void addComponent(Component component) {
        components.add(component);
        component.setParent(this);
    }

    public void addComponents(Component... components) {
        for (Component c : components) {
            addComponent(c);
        }
    }

    /**
     * gets component of type
     *
     * @param type the type of the desired component
     * @return the component not cast
     */
    public Component getComponent(ComponentType type) {
        for (Component c : components) {
            if (c.getType() == type) {
                return c;
            }
        }
        return null;
    }

    /**
     * Gets the first component that is of the same type as T
     *
     * @param type [T].class
     * @param <T>  the type of the desired component
     * @return the component cast to the appropriate type
     */
    @SuppressWarnings("unchecked")
    public <T> T getComponent(Class<T> type) {
        for (Component c : components) {
            if (type.isInstance(c)) {
                return (T) c;
            }
        }
        return null;
    }

    /**
     * Gets the list of components that is of the same type as T
     *
     * @param type [T].class
     * @param <T>  the type of the desired component
     * @return the components cast to the appropriate type
     */
    @SuppressWarnings("unchecked")
    public <T> ArrayList<T> getComponents(Class<T> type) {
        ArrayList<T> res = new ArrayList<>();
        for (Component c : components) {
            if (type.isInstance(c)) {
                res.add((T) c);
            }
        }
        return res;
    }

    /**
     * Raises the appropriate events on each component with exception to rendering
     */
    public final void raiseEvents(ComponentEvent... events) {
        for (ComponentEvent event : events) {
            for (Component c : components) {
                switch (event) {
                    case Awake:
                        c.awake();
                        break;
                    case Start:
                        c.start();
                        break;
                    case Update:
                        c.update();
                        break;
                }
            }
        }
    }

    /**
     * Similar to the Component's cleanUp event
     */
    public void cleanUp() {

    }

    /**
     * Similar to the Component's update event
     */
    public void update() {

    }
}
