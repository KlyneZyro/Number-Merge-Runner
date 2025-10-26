package application;

class Checkpoint {
    private Block leftBlock;
    private Block rightBlock;

    public Checkpoint(Block leftBlock, Block rightBlock) {
        this.leftBlock = leftBlock;
        this.rightBlock = rightBlock;
    }

    public Block getLeftBlock() {
        return leftBlock;
    }

    public Block getRightBlock() {
        return rightBlock;
    }
}