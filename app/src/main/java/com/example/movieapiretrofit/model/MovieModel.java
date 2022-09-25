package com.example.movieapiretrofit.model;

import java.util.List;

public class MovieModel {

    private Integer total_result;
    private Integer total_pages;
    private List<Results> results;

    public Integer getTotal_result() {
        return total_result;
    }

    public void setTotal_result(Integer total_result) {
        this.total_result = total_result;
    }

    public Integer getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(Integer total_pages) {
        this.total_pages = total_pages;
    }

    public List<Results> getResults() {
        return results;
    }

    public void setResult(List<Results> results) {
        this.results = results;
    }

    public class Results{

        private Integer id;
        private String backdrop_path;
        private String poster_path;
        private String title;
        private String overview;
        private String release_date;

        public Results(String poster_path) {
            this.poster_path = poster_path;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getBackdrop_path() {
            return backdrop_path;
        }

        public void setBackdrop_path(String backdrop_path) {
            this.backdrop_path = backdrop_path;
        }

        public String getPoster_path() {
            return poster_path;
        }

        public void setPoster_path(String poster_path) {
            this.poster_path = poster_path;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getOverview() {
            return overview;
        }

        public void setOverview(String overview) {
            this.overview = overview;
        }

        public String getRelease_date() {
            return release_date;
        }

        public void setRelease_date(String release_date) {
            this.release_date = release_date;
        }

        @Override
        public String toString() {
            return "Results{" +
                    "id=" + id +
                    ", backdrop_path='" + backdrop_path + '\'' +
                    ", title='" + title + '\'' +
                    ", overview='" + overview + '\'' +
                    ", release_date='" + release_date + '\'' +
                    '}';
        }
    }
}
