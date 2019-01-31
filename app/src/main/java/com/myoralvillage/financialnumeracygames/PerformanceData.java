/*
 * Copyright 2019 MyOralVillage
 * All Rights Reserved
 */

package com.myoralvillage.financialnumeracygames;

/*
 * This is the class used to contain all the performance data
 *
 * For the moment I'm going to make it a singleton class. I certainly don't
 * want the overhead of instantiating this at every context switch
 *
 */

public class PerformanceData {
    private static PerformanceData INSTANCE = new PerformanceData();
    private int group_id, user_id;

    public static PerformanceData getInstance() {
        // TODO - Why did the static intialization not work?
        if(INSTANCE == null) {
            INSTANCE = new PerformanceData();
        }
        return INSTANCE;
    }

    public void setUserID(int id) {
        user_id = id;
    }

    public int getUserId() {
        return user_id;
    }

    public void setGroupId(int id) {
        group_id = id;
    }

    public int getGroupId(int id) {
        return group_id;
    }

     /*
     * This code is for only a single group
     * 
     * I considered keeping the information for all groups in a single structure but realized 
     * that is quite inefficient.
     * 
     * Within the tablet we will only ever care about information WITHIN a group,
     * 
     * Separate groups will probably be handled by separate files. That makes getting
     * the information OFF the tablet a little harder but I can live with that.
     *
     * For each user, we keep an array of games.
     *
     * For each game, we keep an array of sub games
     *
     * For each subgame, we keep the actual data
     */


    public class PerfDataKept {
        private byte num_successes;
        private byte num_failures;
        private short best_time; // milliseconds
        // In a REAL language I'd use an array. But Java makes this more convenient
        private short most_recent;
        private short second_most_recent;
        private short third_most_recent;
    }

    /*
     * For the moment, lets just keep fixed size arrays. Wastes storage in
     * the general case but is more efficient and it isn't all that much storage
     * AND we need that storage in the worst case anyway.
     *
     */

   // private final int num_groups = 30;
    private final int num_users = 25;   // per group
    private final int num_games = 9;
    private final int num_sub_games = 10;

    private PerfDataKept[][][] actual_data; // User, Game, Subgame

    private PerformanceData() {
        actual_data = new PerfDataKept[num_users][num_games][num_sub_games];
        /*
         * The above initizliaes the top two levels but fails with the final level (because we need to
         * call the constructor manually. I think. So, we have to
         * initialize them manually
         */
       for(int i = 0; i < num_users; i++) {
           for (int j = 0; j < num_games; j++) {
               for (int k = 0; k < num_sub_games; k++) {
                   actual_data[i][j][k] = new PerfDataKept();
               }
           }
       }
    }

    public void test_failed(int test_id, int subtest_id) {
        actual_data[user_id][test_id][subtest_id].num_failures++;
        System.out.println("test failed " + group_id +  "," + user_id + "," + test_id + "," + subtest_id + " failures now " +
                actual_data[user_id][test_id][subtest_id].num_failures);
    }

    public void set_time(int test_id, int subtest_id, long time_diff) {
        actual_data[user_id][test_id][subtest_id].num_successes++;
        if(time_diff == 0) {
            return; // TODO - This is probably an error. Do something?
        }
        if(time_diff > Short.MAX_VALUE)
            time_diff = Short.MAX_VALUE;    // if it takes more than 16 seconds, we don't care
        if((actual_data[user_id][test_id][subtest_id].best_time == 0) ||
                (time_diff < actual_data[user_id][test_id][subtest_id].best_time)) {
            actual_data[user_id][test_id][subtest_id].best_time = (short) time_diff;
        }

        actual_data[user_id][test_id][subtest_id].third_most_recent =
                actual_data[user_id][test_id][subtest_id].second_most_recent;
        actual_data[user_id][test_id][subtest_id].second_most_recent =
                actual_data[user_id][test_id][subtest_id].most_recent;
        actual_data[user_id][test_id][subtest_id].most_recent = (short) time_diff;
        System.out.println("Test succeeded " + actual_data[user_id][test_id][subtest_id] );
    }

    /*
     * These DO all take the user_id since I may well want to iterate over them
     */
    public short get_best_time(int user_id_r, int test_id, int subtest_id) {
        return actual_data[user_id_r][test_id][subtest_id].best_time;
    }

    public short get_most_recent_time(int user_id_r, int test_id, int subtest_id) {
        return actual_data[user_id_r][test_id][subtest_id].most_recent;
    }

    /*
     * Returns the average of the last 3 times (or the last 1 or 2 if the game hasn't been
     * played 3 times
     */
    public short get_average_time(int user_id_r, int test_id, int subtest_id) {
        int tot = actual_data[user_id_r][test_id][subtest_id].most_recent;
        if(tot == 0) {
            return 0;   // Never succeeded
        }
        if(actual_data[user_id_r][test_id][subtest_id].second_most_recent == 0) {
            return (short) tot;
        }
        tot += actual_data[user_id_r][test_id][subtest_id].second_most_recent;
        if(actual_data[user_id_r][test_id][subtest_id].third_most_recent == 0) {
            return (short) ((tot + 1) / 2);
        }
        tot += actual_data[user_id_r][test_id][subtest_id].third_most_recent;
        return (short) ((tot + 1) / 3);
    }
}
