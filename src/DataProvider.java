public class DataProvider {
//    T: Tank
//    S: Soldier
//    A: Artillery
//    I: Infantry
//    W: Walker
//    L: Laser
//    C: Clear
    public enum Obj {
        TANK(1,"T"),
        SOLDIER(2,"S"),
        ARTILLERY(3,"A"),
        INFANTRY(4,"I"),
        WALKER(5,"W"),
        LASER(6,"L"),
        CLEAR(7,"C");
        private int index;
        private String name;
        private Obj(int index,String name){
            this.index = index;
            this.name = name;
        }

        public static String getNameByIndex(int index){
            for (Obj c : Obj.values()) {
                if (c.getIndex() == index) {
                    return c.name;
                }
            }
            return null;
        }

    public int getIndex() {
        return index;
    }
}

    public static void main(String[] args) {
        System.out.println( new DataProvider().responseScan());
    }
    public DataProvider() {
    }

    public String filterCommand(String str){
        String output = "";
        switch (str){
            case "1":
                output = CommandData.ATTACK;
                break;
            case "2":
                output = CommandData.RETREAT;
                break;
            case "3":
                output = responseScan();
                break;
            default:
                output = "MSG WRONG";
                break;
        }
        return output;
    }

    public String convertCommand(String str){
        String output = "";
        switch (str){
            case CommandData.ATTACK:
                output = CommandData.ATTACK;
                break;
            case CommandData.RETREAT:
                output = CommandData.RETREAT;
                break;
            case CommandData.SCANAREA:
                output = CommandData.SCANAREA;
                break;
            default:
                output = "MSG WRONG";
                break;
        }
        return output;
    }

    public String responseScan(){
        //3 1535334221 x28.543 y97.765 T2
        long unixTime = System.currentTimeMillis() / 1000L;
        double x = Math.round((Math.random() * 100));
        double y = Math.round((Math.random() * 100));
        int num = (int)(Math.random() * 10);
        int index = (int)(Math.random() * Obj.values().length);
        return "3 " + unixTime + " x" + x + " y" + y + " " + Obj.getNameByIndex(index) + num;
    }
}
