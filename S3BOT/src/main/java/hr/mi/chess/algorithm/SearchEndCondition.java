package hr.mi.chess.algorithm;

public class SearchEndCondition {
    private long maxTime = Long.MAX_VALUE;
    private int maxDepth = Integer.MAX_VALUE;
    private long maxNodes = Long.MAX_VALUE;
    private volatile boolean manualStop = false;

    public long getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(long maxTime) {
        this.maxTime = maxTime;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public long getMaxNodes() {
        return maxNodes;
    }

    public void setMaxNodes(long maxNodes) {
        this.maxNodes = maxNodes;
    }

    public boolean isManualStop() {
        return manualStop;
    }

    public void setManualStop(boolean manualStop) {
        this.manualStop = manualStop;
    }
}
