package com.example.mwidlok.teambuilder;

import android.util.Log;

import com.example.mwidlok.teambuilder.Model.Person;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Mike on 11.09.2017.
 */

public class BusinessLogic {

    private static int FACTOR_PROFI = 3;
    private static int FACTOR_AVERAGE = 2;
    private static int FACTOR_AMATEUR = 1;

    public ArrayList<ArrayList<Person>> createTeams(ArrayList<Person> persons, int teamCount)
    {
        if (teamCount > persons.size())
        {
            Log.e("TeamBuilder","Error. Given TeamCount is greater than the amount of persons we have.");
            return null;
        }

        // creates Arrays for each team related to given teamCount..
        ArrayList<ArrayList<Person>> teams = new ArrayList<ArrayList<Person>>();
        for (int i = 0; i < teamCount; ++i)
        {
            teams.add(new ArrayList<Person>());
        }

        // now check all persons in persons array. move them into the proper array

        ArrayList<Person> profis = new ArrayList<Person>();
        ArrayList<Person> average = new ArrayList<Person>();
        ArrayList<Person> amateur = new ArrayList<Person>();



        for (Person person : persons)
        {
            switch (person.getSkillLevel())
            {
                case Profi:
                        profis.add(person);
                        Log.i("TeamBuilder","Array Profis gets Person " +person.getFirstName());
                        break;

                case Average:
                    average.add(person);
                    Log.i("TeamBuilder","Array Average gets Person " +person.getFirstName());
                    break;

                case Amateur:
                    amateur.add(person);
                    Log.i("TeamBuilder","Array Amateur gets Person " +person.getFirstName());
            }
        }

        int counter = 0;
        // Take all profis and seperate them into each existing team array as far as possible.
        for (int i = 0; i < profis.size(); ++i)
        {
            if (counter == teams.size())
                counter = 0;

            teams.get(counter).add(profis.get(i));
            counter++;
        }

        // Now take all amateure and separate them into each existing team array as far as possible.
        counter = 0;
        for (int i = 0; i < amateur.size(); ++i)
        {
            if (counter == teams.size())
                counter = 0;

            teams.get(counter).add(amateur.get(i));
            counter++;
        }

        // Now take all averages and separate them into each existing team array as far as possible.

        counter = 0;
        int teamSizes[] = new int[teams.size()];
        // getting first team which deserves adding a average to it..
        // a team which has not the same count of members yet as the others
        for (int z = 0; z < teams.size(); ++z)
        {
            teamSizes[z] = teams.get(z).size();
            Log.i("TeamBuilder","Team " + (z+1) + " has " + teamSizes[z] + " members");
        }

        for (int i = 0; i < average.size(); ++i)
        {
            teams.get(counter).add(average.get(i));
            counter++;
        }

        validateTeams(teams);

        int zaehler = 0;
        for (ArrayList<Person> team : teams)
        {
            zaehler++;
            for (Person p : team)
            {
                Log.i("TeamBuilder","We have a person called " + p.getFirstName() + "in team " + zaehler + " with a " + p.getSkillLevel());
            }
        }

        return teams;
    }



    private int getTeamStrength(ArrayList<Person> team)
    {
        int strength = 0;
        for (Person p : team)
            if(p.getSkillLevel() == Person.SkillLevel.Profi)
                strength += FACTOR_PROFI;
            else if (p.getSkillLevel() == Person.SkillLevel.Average)
                strength += FACTOR_AVERAGE;
            else if (p.getSkillLevel() == Person.SkillLevel.Amateur)
                strength += FACTOR_AMATEUR;

        return strength;
    }

    private boolean isAmateurInTeam(ArrayList<Person> team)
    {
        boolean isAmateur = false;

        for (Person p : team)
        {
            if (p.getSkillLevel() == Person.SkillLevel.Amateur)
            {
                isAmateur = true;
                break;
            }
        }

        return isAmateur;
    }

    private ArrayList<ArrayList<Person>> validateTeams(ArrayList<ArrayList<Person>> teamList)
    {
        ArrayList<ArrayList<Person>> list = new ArrayList<ArrayList<Person>>();
        ArrayList<Person> team1 = teamList.get(0);
        ArrayList<Person> team2 = teamList.get(1);

        //every team needs to check for its strength.
        // a profi member has a value of 3
        // a average member has a value of 2
        // a amateur member has a value of 1

        //lets do this with only 2 teams for the beginning

        int team1Strength = getTeamStrength(team1);
        int team2Strength = getTeamStrength(team2);

        Log.i("TeamBuilder","Power of Team 1 : " + team1Strength);
        Log.i("TeamBuilder","Power of Team 2: " + team2Strength);

        int diff = team1Strength-team2Strength;

        // now try to get an amateur of team 1 and transfer it to team 2

        if (isAmateurInTeam(team1))
        {
            // Team 1 has at least one Amateur. Take him, transfer him into team 2 and remove him from team 1.
            try
            {
                Person amateur = team1.get(team1.size()-1);
                team2.add(amateur);
                team1.remove(amateur);
            }
            catch(Exception exc)
            {
                Log.e("TeamBuilder","Couldn't get amateur member of team1");
            }
        }

        // now check once again the strenth of both teams. If the difference is still greater than 2, repeat the procudure.

        Log.i("TeamBuilder","Now Team 1 Strength is " + getTeamStrength(team1));
        Log.i("TeamBuilder","Now Team 2 Strength is " + getTeamStrength(team2));

        return list;
    }
    private boolean haveAllTeamsSameSize(ArrayList<ArrayList<Person>> teams)
    {
        boolean allTeamsHaveSameSize = true;

        int refTeamAmount = teams.get(teams.size()-1).size();
        for (ArrayList<Person> team : teams)
        {
            if (refTeamAmount != team.size())
            {
                allTeamsHaveSameSize = false;
                break;
            }
        }

        return allTeamsHaveSameSize;
    }
}
