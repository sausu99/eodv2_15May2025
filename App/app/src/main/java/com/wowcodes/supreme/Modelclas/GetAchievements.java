package com.wowcodes.supreme.Modelclas;

import java.util.ArrayList;

public class GetAchievements {

    private ArrayList<GetAchievements.Get_Achievements_Inner> JSON_DATA;

    public ArrayList<GetAchievements.Get_Achievements_Inner> getJSON_DATA() {
        return JSON_DATA;
    }

    public void setJSON_DATA(ArrayList<GetAchievements.Get_Achievements_Inner> JSON_DATA) {
        this.JSON_DATA = JSON_DATA;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [JSON_DATA = "+JSON_DATA+"]";
    }

    public class Get_Achievements_Inner {
        String achievement_id,name,description,color,image,category,goal,points,status,progress,earned_at;

        public String getAchievement_id() {
            return achievement_id;
        }

        public void setAchievement_id(String achievement_id) {
            this.achievement_id = achievement_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getGoal() {
            return goal;
        }

        public void setGoal(String goal) {
            this.goal = goal;
        }

        public String getPoints() {
            return points;
        }

        public void setPoints(String points) {
            this.points = points;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getProgress() {
            return progress;
        }

        public void setProgress(String progress) {
            this.progress = progress;
        }

        public String getEarned_at() {
            return earned_at;
        }

        public void setEarned_at(String earned_at) {
            this.earned_at = earned_at;
        }

        @Override
        public String toString() {
            return "Get_Achievements_Inner{" +
                    "achievement_id='" + achievement_id + '\'' +
                    ", name='" + name + '\'' +
                    ", description='" + description + '\'' +
                    ", color='" + color + '\'' +
                    ", image='" + image + '\'' +
                    ", category='" + category + '\'' +
                    ", goal='" + goal + '\'' +
                    ", points='" + points + '\'' +
                    ", status='" + status + '\'' +
                    ", progress='" + progress + '\'' +
                    ", earned_at='" + earned_at + '\'' +
                    '}';
        }
    }
}
