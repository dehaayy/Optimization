public class Edge {
    int source;
    int destination;
    int flow_rate;
    public Edge(int source, int destination, int flow_rate)
    {
        this.source = source;
        this.destination = destination;
        this.flow_rate = flow_rate;
    }
    public Edge(int destination, int flow_rate)
    {
        this.destination = destination;
        this.flow_rate = flow_rate;
    }
    public Edge(int destination)
    {
        this.destination = destination;
    }
    public Edge()
    {
        this.destination = 0;
        this.flow_rate = 0;
    }
}
