package src;

public class RAM {

    //TODO Converts DNA substrings for GeneBankCreate driver

    public long getKey() {
        return key;
    }

    private long key = 0;




    public long convertGBKtoSubseq(String input)
    {
        input = input.toUpperCase();
        long key = 0x00000000;
        for(int i = 0; i<input.length(); i++){
            //convert String char to 0,1,2,3
            //switch statements

            //key |= position(position = tbits << 2i)
        }



        return key;

    }


    public long convertLongtoString(Long input, int length)
    {
        String convert;
        long temp;

        return input;

    }

}