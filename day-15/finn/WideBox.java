import java.util.List;

public class WideBox {

    private EmptyPosition leftPosition;
    private EmptyPosition rigthPosition;

    public WideBox(EmptyPosition rigthPosition, EmptyPosition leftPosition) {
        this.rigthPosition = rigthPosition;
        this.leftPosition = leftPosition;
    }

    public EmptyPosition getLeftPosition() {
        return leftPosition;
    }

    public boolean canMove(MovementDirection direction, List<List<Position>> map) {
        if (direction.equals(MovementDirection.UP)) {
            int newY = this.leftPosition.getY() - 1;
            return handleWideUpAndDownMovementCheck(direction, map, newY);
        } else if (direction.equals(MovementDirection.DOWN)) {
            int newY = this.leftPosition.getY() + 1;
            return handleWideUpAndDownMovementCheck(direction, map, newY);
        } else if (direction.equals(MovementDirection.LEFT)) {
            int newX = this.leftPosition.getX() - 1;
            if (newX < 0) {
                return false;
            }
            Position targetPosition = map.get(leftPosition.getY()).get(newX);
            if (targetPosition instanceof EmptyPosition emptyTargetPos) {
                if (emptyTargetPos.hasWideBox()) {
                    return emptyTargetPos.getWideBox().canMove(direction, map);
                }
                return true;
            }
        } else if (direction.equals(MovementDirection.RIGHT)) {
            int newX = this.rigthPosition.getX() + 1;
            if (newX >= map.getFirst().size()) {
                return false;
            }
            Position targetPosition = map.get(rigthPosition.getY()).get(newX);
            if (targetPosition instanceof EmptyPosition emptyTargetPos) {
                if (emptyTargetPos.hasWideBox()) {
                    return emptyTargetPos.getWideBox().canMove(direction, map);
                }
                return true;
            }
        }
        return false;
    }

    private boolean handleWideUpAndDownMovementCheck(MovementDirection direction, List<List<Position>> map, int newY) {
        if (newY < 0 || newY >= map.size()) {
            return false;
        }
        Position leftTargetPosition = map.get(newY).get(leftPosition.getX());
        Position rightTargetPosition = map.get(newY).get(rigthPosition.getX());
        if (!(leftTargetPosition instanceof EmptyPosition && rightTargetPosition instanceof EmptyPosition)) {
            return false;
        }
        EmptyPosition leftEmptyTargetPos = (EmptyPosition) leftTargetPosition;
        EmptyPosition rightEmptyTargetPos = (EmptyPosition) rightTargetPosition;

        if (leftEmptyTargetPos.hasWideBox()) {
            if (rightEmptyTargetPos.hasWideBox()) {
                if(leftEmptyTargetPos.getWideBox().equals(rightEmptyTargetPos.getWideBox())) {
                    return leftEmptyTargetPos.getWideBox().canMove(direction, map);
                } else return leftEmptyTargetPos.getWideBox().canMove(direction, map) && rightEmptyTargetPos.getWideBox().canMove(direction, map);
            } else return leftEmptyTargetPos.getWideBox().canMove(direction, map);
        } else if (rightEmptyTargetPos.hasWideBox()) {
            return rightEmptyTargetPos.getWideBox().canMove(direction, map);
        }

        return true;
    }

    public boolean moveBox(MovementDirection direction, List<List<Position>> map) {
        if (direction.equals(MovementDirection.UP)) {
            int newY = this.leftPosition.getY() - 1;
            return handleWideUpAndDownMovement(direction, map, newY);
        } else if (direction.equals(MovementDirection.DOWN)) {
            int newY = this.leftPosition.getY() + 1;
            return handleWideUpAndDownMovement(direction, map, newY);
        } else if (direction.equals(MovementDirection.LEFT)) {
            int newX = this.leftPosition.getX() - 1;
            if (newX < 0) {
                return false;
            }
            Position targetPosition = map.get(leftPosition.getY()).get(newX);
            if (targetPosition instanceof EmptyPosition emptyTargetPos) {
                if (emptyTargetPos.hasWideBox()) {
                    if (!emptyTargetPos.getWideBox().moveBox(direction, map)) {
                        return false;
                    }
                }

                this.rigthPosition.setWideBox(null);
                emptyTargetPos.setWideBox(this);
                this.rigthPosition = this.leftPosition;
                this.leftPosition = emptyTargetPos;
                return true;
            }
        } else if (direction.equals(MovementDirection.RIGHT)) {
            int newX = this.rigthPosition.getX() + 1;
            if (newX >= map.getFirst().size()) {
                return false;
            }
            Position targetPosition = map.get(rigthPosition.getY()).get(newX);
            if (targetPosition instanceof EmptyPosition emptyTargetPos) {
                if (emptyTargetPos.hasWideBox()) {
                    if (!emptyTargetPos.getWideBox().moveBox(direction, map)) {
                        return false;
                    }
                }

                this.leftPosition.setWideBox(null);
                emptyTargetPos.setWideBox(this);
                this.leftPosition = rigthPosition;
                this.rigthPosition = emptyTargetPos;
                return true;
            }
        }
        return false;
    }

    private boolean handleWideUpAndDownMovement(MovementDirection direction, List<List<Position>> map, int newY) {
        if (newY < 0 || newY >= map.size()) {
            return false;
        }
        Position leftTargetPosition = map.get(newY).get(leftPosition.getX());
        Position rightTargetPosition = map.get(newY).get(rigthPosition.getX());
        if (!(leftTargetPosition instanceof EmptyPosition && rightTargetPosition instanceof EmptyPosition)) {
            return false;
        }
        EmptyPosition leftEmptyTargetPos = (EmptyPosition) leftTargetPosition;
        EmptyPosition rightEmptyTargetPos = (EmptyPosition) rightTargetPosition;

        if (leftEmptyTargetPos.hasWideBox()) {
            if (rightEmptyTargetPos.hasWideBox()) {
                if(leftEmptyTargetPos.getWideBox().equals(rightEmptyTargetPos.getWideBox())) {
                    if (!leftEmptyTargetPos.getWideBox().moveBox(direction, map)) {
                        return false;
                    }
                } else if (!(leftEmptyTargetPos.getWideBox().canMove(direction, map) && rightEmptyTargetPos.getWideBox().canMove(direction, map))) {
                    return false;
                } else {
                    leftEmptyTargetPos.getWideBox().moveBox(direction, map);
                    rightEmptyTargetPos.getWideBox().moveBox(direction, map);
                }
            } else if (!leftEmptyTargetPos.getWideBox().moveBox(direction, map)) {
                return false;
            }
        } else if (rightEmptyTargetPos.hasWideBox()) {
            if (!rightEmptyTargetPos.getWideBox().moveBox(direction, map)) {
                return false;
            }
        }

        this.leftPosition.setWideBox(null);
        this.rigthPosition.setWideBox(null);
        leftEmptyTargetPos.setWideBox(this);
        rightEmptyTargetPos.setWideBox(this);
        this.leftPosition = leftEmptyTargetPos;
        this.rigthPosition = rightEmptyTargetPos;
        return true;
    }

}
