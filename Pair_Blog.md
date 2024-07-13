# Assignment II Pair Blog Template

## Task 1) Code Analysis and Refactoring ⛏️

### a) From DRY to Design Patterns

[Links to your merge requests](https://nw-syd-gitlab.cseunsw.tech/COMP2511/24T2/teams/W15B_MUSHROOM/assignment-ii/-/merge_requests/2)

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

[Links to your merge requests](https://nw-syd-gitlab.cseunsw.tech/COMP2511/24T2/teams/W15B_MUSHROOM/assignment-ii/-/merge_requests/3)

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

| Entities       | onOverlap | onMovedAway | onDestroy |
|----------------|-----------|-------------|-----------|
| **Static**     |           |             |           |
| Wall           |           |             |           |
| Exit           |           |             |           |
| Boulder        | ✔️         |             |           |
| Switch         | ✔️         | ✔️           |           |
| Door           | ✔️         |             |           |
| Portal         | ✔️         |             |           |
| Zombie Toast Spawner |     |             | ✔️         |
| **Moving**     |           |             |           |
| Enemy          | ✔️         |             | ✔️         |
| Player         | ✔️         |             |           |
| **Collectable**|           |             |           |
| Treasure       | ✔️         |             |           |
| Key            | ✔️         |             |           |
| Potion         | ✔️         |             |           |
| Wood           | ✔️         |             |           |
| Arrows         | ✔️         |             |           |
| Bomb           | ✔️         |             |           |
| Sword          | ✔️         |             |           |
| **Buildables** |           |             |           |
| Bow            |           |             |           |
| Shield         |           |             |           |


1. Provided default implementations for `onOverlap()`, `onMovedAway()` and `onDestroy()` in the `Entity` superclass.
2. Only the subclasses that require specific behaviour will override the methods with their custom implementation.
3. Created a `Collectable` class:
    - The `Collectable` superclass extends `Entity` and implements `InventoryItem`.
    - Overrides the `onOverlap()` method to provide a default implementation.

### d) More Code Smells

[Links to your merge requests](https://nw-syd-gitlab.cseunsw.tech/COMP2511/24T2/teams/W15B_MUSHROOM/assignment-ii/-/merge_requests/4)

> i. What design smell is present in the above description?

The code smell present in the above description is "**Shotgun Surgery**". This is because a single change requires making many small changes to many different classes.

In this case, the current `pickUp` method in the `Player` class is tightly coupled with the `Treasure` entity class. The `pickUp` method directly checks if the item is an instance of `Treasure`. This means that the `Player` class is responsible for knowing and handling specific types of entities. If we want to modify how an entity is picked up or introduce a new entity type, we would need to modify the existing code in `Player` as well as the separate entity classes that would be affected.


> ii. Refactor the code to resolve the smell and underlying problem causing it.

Refactored the `pickUp` method to handle only the addition of the item to the inventory to reduce coupling:

1. Defined a `Collectable` class that extends `InventoryItem` to represent collectable entities that can be picked up and placed in the player's inventory.
2. Ensured `Collectable` entities implement `onOverlap()` method to handle their own pickup logic.
3. Modified `pickup()` method to handle pickup logic at player level by directly adding the item to inventory with type-safety checks:
    ```java
    public boolean pickUp(Entity item) {
        if (item instanceof InventoryItem) {
            return inventory.add((InventoryItem) item);
        }
        return false;
    }
    ```
4. Removed the `CollectedTreasureCount` field in `Player` and modified `getCollectedTreasureCount()` to return the number inventory items that are of type `Treasure`:
    ```java
    public int getCollectedTreasureCount() {
        return inventory.count(Treasure.class);
    }
    ```

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

[Merge Request 1](https://nw-syd-gitlab.cseunsw.tech/COMP2511/24T2/teams/W15B_MUSHROOM/assignment-ii/-/merge_requests/5)

Refactored the State Pattern for implementing the effects of potions (invisibility and invincibility) for the Player.
In the `PlayerState` Classes:
- Removed boolean flags (`isInvincible`, `isInvisible`)
- Removed all transition methods (`transitionBase()`, `transitionInvincible()`, `transitionInvisible()`) since the `Player` context class should handle all state transitions
- Implemented the state-related behaviour for the method `applyBuff()` in the State classes
- Initialised the `Player` with the base state in the constructor

[Merge Request 2](https://nw-syd-gitlab.cseunsw.tech/COMP2511/24T2/teams/W15B_MUSHROOM/assignment-ii/-/merge_requests/6)

Refactored the `Buildable` Entity classes to reduce the hardcoding, promote code reuse and improve the flexibility and maintainability.

- Implemented the Builder Pattern to remove hardcoding of battle statistics:
    - Created a new `BattleStatisticsBuilder` class to construct `BattleStatistics` objects with varying attributes.
    - Modified all `BattleItem` classes to take a `BattleStatisticsBuilder` object in its constructor.
    - Each buildable entity now initialises its battle statistics attributes using the `BattleStatisticsBuilder`.
    - Delegated the creation of `BattleStatistics` to the builder, promoting code reuse.
    - Moved the `applyBuff()` method to the `Buildable` superclass.

- Removed hardcoding of durability:
    - Modified the `Buildable` class to take in the durability attribute through its constructor.
    - Moved the `use()` and `getDurability()` methods to the `Buildable` superclass to eliminate code duplication in the subclasses.

[Merge Request 3](https://nw-syd-gitlab.cseunsw.tech/COMP2511/24T2/teams/W15B_MUSHROOM/assignment-ii/-/merge_requests/7)

Removed and replaced `Collectable` with `InventoryItem` to represent items that can be picked up.
Removed code duplication across `InventoryItem`, `BattleItem` and `Buildable`.

Modified `Buildable` to be an interface instead of an abstract class:
- Extracted the previous methods to `BattleItem` for the subclasses Bow and Shield

Modified `InventoryItem` to be an abstract class instead of an interface:
- Represents items that are collectable and can be picked up.
- Defined `onOverlap`, `canMoveOnto` and `use` methods with default implementations.

Modified `BattleItem` to be an abstract class instead of an interface:
- Extends from `InventoryItem` to represent inventory items that have buffs.
- Defined `getDurability`, `applyBuff` methods with default implementations.
- Override `use` to decrease durability.

[Merge Request 4](https://nw-syd-gitlab.cseunsw.tech/COMP2511/24T2/teams/W15B_MUSHROOM/assignment-ii/-/merge_requests/10)

1) The previous design of the `Inventory` class knew about the internal workings of the inventory items, such as the specific requirements for building items and which items are weapons. This is a code smell because it tightly couples the `Inventory` class to the individual item classes. It also violates the SRP and reduces the flexibility and maintainability of the `Inventory` class.

    - Delegated the build requirements checks to the respective classes `Bow` and `Shield` instead:
        *before:*
        ```java
        public List<String> getBuildables() {
            int wood = count(Wood.class);
            int arrows = count(Arrow.class);
            int treasure = count(Treasure.class);
            int keys = count(Key.class);
            List<String> result = new ArrayList<>();

            if (wood >= 1 && arrows >= 3) {
                result.add("bow");
            }
            if (wood >= 2 && (treasure >= 1 || keys >= 1)) {
                result.add("shield");
            }
            return result;
        }
        ```
        *after:*
        ```java
        public List<String> getBuildables() {
            List<String> result = new ArrayList<>();
            if (Bow.isBuildable(this)) {
                result.add("bow");
            }
            if (Shield.isBuildable(this)) {
                result.add("shield");
            }
            return result;
        }
        ```

    - Encapsulated the build logic in each item clas to simplify the `checkBuildCriteria()` method: `Bow.build(factory, this)`


    - Fixed the logic in `build()` in `Player` to check the build criteria for the passed entity and removed the boolean flag `remove` from `checkBuildCriteria()` since it is always true:
        *before:*
        ```java
        public boolean build(String entity, EntityFactory factory) {
            InventoryItem item = inventory.checkBuildCriteria(this, true, entity.equals("shield"), factory);
            // ...
        }
        ```
        *after:*
        ```java
        public boolean build(String entity, EntityFactory factory) {
            InventoryItem item = inventory.checkBuildCriteria(this, entity, factory);
            // ...
        }
        ```

    - Refactored `hasWeapon()` and `getWeapon()` methods to not depend on specific item types (`Sword` or `Bow`) in order to determine if an item is a weapon. Instead, the checks for weapon are now delegated to the `BattleItem` classes.

2) Reduced the length of the **long methods** `onPutDown()` and `explode()` in the `Bomb` class by isolating the logic for subscribing to switches and destroying entities and extracting them to helper methods `subscribeToAdjacentSwitches()` and `destroyEntitiesInRadius()`.

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
