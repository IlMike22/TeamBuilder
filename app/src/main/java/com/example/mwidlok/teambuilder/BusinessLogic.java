package com.example.mwidlok.teambuilder;

import android.util.Log;

import com.example.mwidlok.teambuilder.Model.Person;

import java.util.ArrayList;

/**
 * Created by Mike on 11.09.2017.
 */

public class BusinessLogic {

    private static final int FACTOR_PROFI = 3;
    private static final int FACTOR_AVERAGE = 2;
    private static final int FACTOR_AMATEUR = 1;
    private static final String TAG = "TeamBuilder";

    public ArrayList<ArrayList<Person>> createTeams(ArrayList<Person> persons) {
        // in version 1 we only have two teams. make an update later with custom team amount.

        ArrayList<Person> team1 = new ArrayList<>();
        ArrayList<Person> team2 = new ArrayList<>();

        // now check all persons in persons array. move them into the proper array

        ArrayList<Person> profis = new ArrayList<>();
        ArrayList<Person> averages = new ArrayList<>();
        ArrayList<Person> amateurs = new ArrayList<>();

        for (Person person : persons) {
            switch (person.getSkillLevel()) {
                case 0:
                    amateurs.add(person);
                    Log.i(TAG, "Array Amateur gets Person " + person.getFirstName());
                    break;

                case 1:
                    averages.add(person);
                    Log.i(TAG, "Array Average gets Person " + person.getFirstName());
                    break;

                case 2:
                    profis.add(person);
                    Log.i(TAG, "Array Profis gets Person " + person.getFirstName());
                    break;

                default:
                    Log.e(TAG, "Invalid skill level detected.");
                    break;
            }
        }

        // Take all profis and seperate them into each existing team array as far as possible.
        for (int i = 0; i < profis.size(); ++i) {
            if (i % 2 == 0)
                team1.add(profis.get(i));
            else
                team2.add(profis.get(i));
        }

        // Take all averages and separate them into each existing team array as far as possible
        for (int i = 0; i < averages.size(); ++i) {
            if (i % 2 == 0)
                team1.add(averages.get(i));
            else
                team2.add(averages.get(i));
        }

        // take all the amateurs and fill both team arrays with them as far as possible
        for (int i = 0; i < amateurs.size(); ++i) {
            if (i % 2 == 0)
                team1.add(amateurs.get(i));
            else
                team2.add(amateurs.get(i));
        }

        validateTeams(team1, team2);

        for (Person person : team1) {
            Log.i("TeamBuilder", "We have a person called " + person.getFirstName() + "in team1" + " with a " + person.getSkillLevel());
        }

        for (Person person : team2) {
            Log.i("TeamBuilder", "We have a person called " + person.getFirstName() + "in team2" + " with a " + person.getSkillLevel());
        }

        ArrayList<ArrayList<Person>> teams = new ArrayList<>();
        teams.add(team1);
        teams.add(team2);

        return teams;
    }

    private int getTeamStrength(ArrayList<Person> team) {
        int strength = 0;
        for (Person p : team)
            switch (p.getSkillLevel()) {
                case 2:
                    strength += FACTOR_PROFI;
                    break;
                case 1:
                    strength += FACTOR_AVERAGE;
                    break;
                case 0:
                    strength += FACTOR_AMATEUR;
                    break;
            }

        return strength;
    }

    private Person getFirstAverageFromTeam(ArrayList<Person> team) {
        for (Person p : team) {
            if (p.getSkillLevel() == 1) {
                return p;
            }
        }

        return null;
    }

    private Person getFirstAmateurFromTeam(ArrayList<Person> team) {
        for (Person person : team) {
            if (person.getSkillLevel() == 0) {
                return person;
            }
        }
        return null;
    }

    private void validateTeams(ArrayList<Person> team1, ArrayList<Person> team2) {

        //every team needs to check for its strength.
        // a profi member has a value of 3
        // a average member has a value of 2
        // a amateur member has a value of 1

        int team1Strength = getTeamStrength(team1);
        int team2Strength = getTeamStrength(team2);

        Log.i("TeamBuilder", "Power of Team 1 : " + team1Strength);
        Log.i("TeamBuilder", "Power of Team 2: " + team2Strength);

        int diff = team1Strength - team2Strength;

        if (diff > 5) {
            Person average = getFirstAverageFromTeam(team1);
            if (average != null) {
                // we found and got one average in team 1 who can be transfered to team 2.
                team1.remove(average);
                team2.add(average);
                Log.i("TeamBuilder", "Transferring Average from team 1 to team 2");
            }
        } else if (diff > 2) {
            Person amateur = getFirstAmateurFromTeam(team1);
            if (amateur != null) {
                team1.remove(amateur);
                team2.add(amateur);
                Log.i("TeamBuilder", "Transferring Amateur from team 1 to team 2");
            }
        } else
            Log.i("TeamBuilder", "Teams are already equal at least quite.");

        // now check once again the strenth of both teams. If the difference is still greater than 2, repeat the procedure.

        Log.i("TeamBuilder", "Now Team 1 Strength is " + getTeamStrength(team1));
        Log.i("TeamBuilder", "Now Team 2 Strength is " + getTeamStrength(team2));

        ArrayList<ArrayList<Person>> teams = new ArrayList<>();
        teams.add(team1);
        teams.add(team2);
    }

    private boolean haveAllTeamsSameSize(ArrayList<ArrayList<Person>> teams) {
        boolean allTeamsHaveSameSize = true;

        int refTeamAmount = teams.get(teams.size() - 1).size();
        for (ArrayList<Person> team : teams) {
            if (refTeamAmount != team.size()) {
                allTeamsHaveSameSize = false;
                break;
            }
        }

        return allTeamsHaveSameSize;
    }
}
