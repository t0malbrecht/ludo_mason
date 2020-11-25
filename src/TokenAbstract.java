import sim.portrayal.Oriented2D;

public abstract class TokenAbstract implements Oriented2D
    {
        private static final long serialVersionUID = 1;

        /** The Action "Go 1 Step" */
        public static final int S1 = 1;
        /** The Action "Go 2 Step" */
        public static final int S2 = 2;
        /** The Action "Go 3 Step" */
        public static final int S3 = 3;
        /** The Action "Go 4 Step" */
        public static final int S4 = 4;
        /** The Action "Go 5 Step" */
        public static final int S5 = 5;
        /** The Action "Go 6 Step" */
        public static final int S6 = 6;

        /** The Action "Do Nothing" */
        public static final int NOTHING = -1;

        /** The last action performed by the agent.  Initially NOTHING. */
        public int lastAction = NOTHING;

        /** The location of the Token.  We store it here to avoid having to do multiple
         Continuous2D lookups, which are expensive.  */

}
