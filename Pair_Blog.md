# Assignment II Pair Blog Template

## Task 1) Code Analysis and Refactoring ⛏️

### a) From DRY to Design Patterns

[Links to your merge requests](/put/links/here)

> i. Look inside src/main/java/dungeonmania/entities/enemies. Where can you notice an instance of repeated code? Note down the particular offending lines/methods/fields.

1. The method `move(Game game)` in `Mercernary` and `ZombieToast` - Random Movement Logic

    The logic for moving to a random position in the method `move(Game game)` is the same in both `Mercernary` and `ZombieToast`.

    In `ZombieToast`, if the player does not have an `InvincibilityPotion`, it moves randomly:
    ```java
    List<Position> pos = getPosition().getCardinallyAdjacentPositions();
    pos = pos.stream().filter(p -> map.canMoveTo(this, p)).collect(Collectors.toList());
    if (pos.size() == 0) {
        nextPos = getPosition();
    } else {
        nextPos = pos.get(randGen.nextInt(pos.size()));
    }
    ```

    In `Mercernary`, if the player has an `InvisibilityPotion`, it moves to a random location:
    ```java
    Random randGen = new Random();
    List<Position> pos = getPosition().getCardinallyAdjacentPositions();
    pos = pos.stream().filter(p -> map.canMoveTo(this, p)).collect(Collectors.toList());
    if (pos.size() == 0) {
        nextPos = getPosition();
        map.moveTo(this, nextPos);
    } else {
        nextPos = pos.get(randGen.nextInt(pos.size()));
        map.moveTo(this, nextPos);
    }
    ```

    The only differences are that in `ZombieToast`, the random generator (`randGen`) is initialised as a class field, whereas in `Mercernary`, it is created locally within the movement logic.
    Additionally, the position is instantly set in `Mercernary` with `map.moveTo(this, nextPos)` once confirmed.

2. The method `move(Game game)` in `Mercernary` and `ZombieToast` - Invincibility Movement Logic

    The logic for handling movement when the player has an `InvincibilityPotion` is the same in both `Mercernary` and `ZombieToast`.
    Specifically, this part of the code which moves the entity away from the player is repeated in the method `move(Game game)`:

    ```java
    if (map.getPlayer().getEffectivePotion() instanceof InvincibilityPotion) {
        Position plrDiff = Position.calculatePositionBetween(map.getPlayer().getPosition(), getPosition());

        Position moveX = (plrDiff.getX() >= 0) ? Position.translateBy(getPosition(), Direction.RIGHT)
                : Position.translateBy(getPosition(), Direction.LEFT);
        Position moveY = (plrDiff.getY() >= 0) ? Position.translateBy(getPosition(), Direction.UP)
                : Position.translateBy(getPosition(), Direction.DOWN);
        Position offset = getPosition();
        if (plrDiff.getY() == 0 && map.canMoveTo(this, moveX))
            offset = moveX;
        else if (plrDiff.getX() == 0 && map.canMoveTo(this, moveY))
            offset = moveY;
        else if (Math.abs(plrDiff.getX()) >= Math.abs(plrDiff.getY())) {
            if (map.canMoveTo(this, moveX))
                offset = moveX;
            else if (map.canMoveTo(this, moveY))
                offset = moveY;
            else
                offset = getPosition();
        } else {
            if (map.canMoveTo(this, moveY))
                offset = moveY;
            else if (map.canMoveTo(this, moveX))
                offset = moveX;
            else
                offset = getPosition();
        }
        nextPos = offset;
    }
    ```

3. The method `onDestroy(GameMap map)` in `Enemy` and `ZombieToastSpawner`

    This method defined in `Enemy` is repeated exactly the same in `ZombieToastSpawner`.
    This is because `ZombieToastSpawner` does not extend from `Enemy` and therefore does not inherit this implementation:

    ```java
    public void onDestroy(GameMap map) {
        Game g = map.getGame();
        g.unsubscribe(getId());
    }
    ```

4. The method `onMovedAway(GameMap map, Entity entity)` in `Enemy` and `ZombieToastSpawner`

    This method is the same in both, which simply returns and does nothing:

    ```java
    public void onMovedAway(GameMap map, Entity entity) {
        return;
    }
    ```

5. Repeated fields for health and attack in `Mercernary`, `Spider` and `ZombieToast`

    For Mercenary:

    ```java
    public static final double DEFAULT_ATTACK = 5.0;
    public static final double DEFAULT_HEALTH = 10.0;
    ```

    For Spider:

    ```java
    public static final double DEFAULT_ATTACK = 5;
    public static final double DEFAULT_HEALTH = 10;
    ```

    For ZombieToast:

    ```java
    public static final double DEFAULT_HEALTH = 5.0;
    public static final double DEFAULT_ATTACK = 6.0;
    ```


> ii. What Design Pattern could be used to improve the quality of the code and avoid repetition? Justify your choice by relating the scenario to the key characteristics of your chosen Design Pattern.

The **Strategy Pattern** can be used to encapsulate different movement behaviors into separate strategy objects, such as moving to a random location, moving away from the player, moving towards the player by calculating the shortest path using Dijkstra's algorithm, and following a fixed trajectory like the spider movement. This enables the Enemy class to delegate its movement to a strategy object, allowing for code reuse, dynamic changes in movement behavior, and enhancing code flexibility and maintainability. Additionally, by centralizing the movement logic into strategy classes, the Strategy Pattern helps avoid code repetition, making the codebase cleaner and easier to manage.


> iii. Using your chosen Design Pattern, refactor the code to remove the repetition.

[Links to your merge requests](https://nw-syd-gitlab.cseunsw.tech/COMP2511/24T2/teams/W15B_MUSHROOM/assignment-ii/-/merge_requests/2)

1. Defined a Strategy Interface called `MovementStrategy` with a `move()` method
2. Created Concrete Strategy Classes for each movement strategy used by the enemy classes:
    - `AlliedMovement`
    - `HostileMovement`
    - `FleeMovement`
    - `RandomMovement`
    - `SpiderMovement`
3. Refactored the Enemy class to use the strategy pattern for movement:
    - Defined an abstract `getMovementStrategy()` method for subclasses to implement
    - Modified the `move()` method to delegate movement behaviour to the strategy object returned by `getMovementStrategy()`


### b) Observer Pattern

> Identify one place where the Observer Pattern is present in the codebase, and outline how the implementation relates to the key characteristics of the Observer Pattern.

The `Game` class implements the Observer Pattern. It acts as the subject, maintaining a list of observers of type `ComparableCallback` and notifying them of state changes, such as a new tick. The `Game` class includes methods to register and unregister observers and a `tick` method to notify them during each state transition.

Observers are encapsulated as `ComparableCallback` objects, containing `Runnable` tasks to be executed. During each tick, these observers respond to the notification by executing their tasks, thereby reacting to the state change in the Game class.

The `Bomb` class implements the Observer Pattern. The `Bomb` class acts as the subject that maintains a list of observers (`subs`), which are `Switch` objects. The `Bomb` class includes methods to subscribe and unsubscribe observers, as well as a `notify` method that notifies its registered observers when the state of the bomb changes in the event of an explosion.


### c) Inheritance Design

[Links to your merge requests](/put/links/here)

> i. Name the code smell present in the above code. Identify all subclasses of Entity which have similar code smells that point towards the same root cause.

The code smell present in the `Exit` entity class is "**Refused Bequest**". This is because `Exit` inherits methods from `Entity` that is does not use or need. Other subclasses of `Entity` which face this problem are:

- `Buildable`
- `Potion`
- `Arrow`
- `Bomb`
- `Key`
- `Sword`
- `Treasure`
- `Wood`
- `Enemy`
- `ZombieToastSpawner`
- `Boulder`
- `Door`
- `Player`
- `Portal`
- `Switch`
- `Wall`

> ii. Redesign the inheritance structure to solve the problem, in doing so remove the smells.

[Briefly explain what you did]

### d) More Code Smells

[Links to your merge requests](/put/links/here)

> i. What design smell is present in the above description?

The code smell present in the above description is "**Shotgun Surgery**". This is because a single change requires making many small changes to many different classes.

> ii. Refactor the code to resolve the smell and underlying problem causing it.

[Briefly explain what you did]
Notes:
We could define a Collectable Interface and update collectable entities which can be picked up to implement it.
The Collectable Interface can define a `onPickUp` method which lets the entitites handle thier own pickup logic.


### e) Open-Closed Goals

[Links to your merge requests](/put/links/here)

> i. Do you think the design is of good quality here? Do you think it complies with the open-closed principle? Do you think the design should be changed?

The current design does not adhere to the open-closed principle.

The open-closed principle states that a class should be open for extension but closed for modification. The current design does not comply with this principle for the following reasons:

1. **Switch Statement**: The `achieved` method in the `Goal` class uses a switch statement based on the type of goal. Every time a new goal type is added, or the behaviour for a goal type changes, this method needs to be modified, which violates the OCP.
2. **Goal Type Logic**: Currently, the behaviour of achieving a goal is hardcoded and locked into a concrete implementation in the Goal class. This means that adding or modifying goal types requires changing the existing Goal class, which is against the OCP.

To better adhere to the open-closed principle, the design should be changed to use inheritance and polymorphism.  Each goal type should be represented by its own class, inheriting from an abstract Goal class. This way, new goal types can be added by creating new subclasses without modifying existing code.


> ii. If you think the design is sufficient as it is, justify your decision. If you think the answer is no, pick a suitable Design Pattern that would improve the quality of the code and refactor the code accordingly.

To better adhere to the open-closed principle, the design should be changed to use inheritance and polymorphism. Each goal type should be represented by its own class, inheriting from an abstract Goal class. This way, new goal types can be added by creating new subclasses without modifying existing code.

### f) Open Refactoring

[Merge Request 1](/put/links/here)

[Briefly explain what you did]

[Merge Request 2](/put/links/here)

[Briefly explain what you did]

Add all other changes you made in the same format here:

## Task 2) Evolution of Requirements 👽

### a) Microevolution - Enemy Goal

[Links to your merge requests](/put/links/here)

**Assumptions**

[Any assumptions made]

**Design**

[Design]

**Changes after review**

[Design review/Changes made]

**Test list**

[Test List]

**Other notes**

[Any other notes]

### Choice 1 (Insert choice)

[Links to your merge requests](/put/links/here)

**Assumptions**

[Any assumptions made]

**Design**

[Design]

**Changes after review**

[Design review/Changes made]

**Test list**

[Test List]

**Other notes**

[Any other notes]

### Choice 2 (Insert choice)

[Links to your merge requests](/put/links/here)

**Assumptions**

[Any assumptions made]

**Design**

[Design]

**Changes after review**

[Design review/Changes made]

**Test list**

[Test List]

**Other notes**

[Any other notes]

### Choice 3 (Insert choice) (If you have a 3rd member)

[Links to your merge requests](/put/links/here)

**Assumptions**

[Any assumptions made]

**Design**

[Design]

**Changes after review**

[Design review/Changes made]

**Test list**

[Test List]

**Other notes**

[Any other notes]

## Task 3) Investigation Task ⁉️

[Merge Request 1](/put/links/here)

[Briefly explain what you did]

[Merge Request 2](/put/links/here)

[Briefly explain what you did]

Add all other changes you made in the same format here:
