package com.vipet.petvip.Restful


// 단순 O / X
data class Result(
    val OK: Boolean
)

// 계정 정보
data class Account(
    val Name: String,
    val ID: String,
    var PW: String?,
    val Phone: String,
    val Addr: String,
    val Cat: Int
)

// 로그인용 정보
data class LoginData(
    val ID: String,
    val PW: String,
    val Token: String
)

// 반려견 정보
data class Pet(
    val ID: Int?,
    val MemberID: String?,
    val Img: String?,
    val Name: String,
    val Birth: String,
    val Species: String,
    val Gender: Char,
    val Weight: Int
)

// 매니저 출력 정보
data class Manager(
    val ID: String,
    val Name: String,
    val Phone: String,
    val Rate: Float
)

// 스케줄 정보
data class PostSchedule(
    val MemberID: String,
    val ManagerID: String,
    val PetID: Int,
    val ServiceType: Int,
    val Start: String,
    val End: String,
    val Price: Int
)

data class Schedule(
    val Service: String,
    val Date: String,
    val Pet: String,
    val Time: String,
    val Manager: String,
    val Month: String,
    val Day: String,
    val ManagerID: String
)

data class Review(
    val MemberID: String,
    val Rating: Float,
    val Content: String,
    val MemberName: String
)

data class ManagerDetail(
    val Name: String,
    val Rate: Float,
    val ScheduleCnt: Int,
    val ReviewCnt: Int,
    val Reviews: List<Review>
)

data class PostReview(
    val MemberID: String,
    val ManagerID: String,
    val Rating: Float,
    val Content: String
)


data class ManagerSchedule(
    val MemberID: String,
    val ServiceType: Int,
    val PetID: Int,
    val Start: String,
    val End: String,
    val PetName: String,
    val MemberName: String,
    val Addr: String,
    val Phone: String
)

data class Benefit(
    val Service: String,
    val Price: Int,
    val Cnt: Int
)

data class Benefits(
    val Month: List<Benefit>,
    val Total: List<Benefit>
)