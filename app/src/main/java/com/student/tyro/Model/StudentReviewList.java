package com.student.tyro.Model;

import java.util.List;

import retrofit2.Call;

public interface StudentReviewList
{
    Call<List<StudentReview>> getAllStudentReview();
}
