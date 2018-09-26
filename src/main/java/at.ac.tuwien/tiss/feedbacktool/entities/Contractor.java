package at.ac.tuwien.tiss.feedbacktool.entities;

/**
 * The class provides a basic implementation of a contractor
 */
public class Contractor {
    private int id;
    private String name;

    /**
     * Creates a contractor with the fields set
     *
     * @param id   the id of the contractor
     * @param name the name of the contractor
     */
    public Contractor(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Gets id
     *
     * @return value of id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the field id to the given parameter
     *
     * @param id the parameter which the field gets assigned to
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets name
     *
     * @return value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the field name to the given parameter
     *
     * @param name the parameter which the field gets assigned to
     */
    public void setName(String name) {
        this.name = name;
    }
}
