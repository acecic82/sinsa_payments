package com.sinsa.sinsa_payments.common.exception.enum

enum class ExceptionCode(
    val message: String
) {
    //SUCCESS
    SUCCESS("성공"),

    //Point_Policy
    POINT_POLICY_INVALID_ACCUMULATED("유효하지 않은 적립금액이 저장 요청되었습니다 Point : "),
    POINT_POLICY_INVALID_HELD("유효하지 않은 보유가능한 금액이 저장 요청되었습니다 Point : "),
    POINT_POLICY_INVALID_DAY_OF_EXPIRED_DATE("유효하지 않은 만료일 저장 요청되었습니다 Days : "),

    //Free_Point
    FREE_POINT_ACCUMULATED_TOO_MUCH("적립 요청한 포인트가 1회 적립 가능한 포인트보다 많습니다. Point : "),
    FREE_POINT_HELD_TOO_MUCH("적립된 포인트가 많아 적립할 수 없습니다."),
    FREE_POINT_ALREADY_USED("적립된 포인트 일부가 사용되어 취소할 수 없습니다."),
    FREE_POINT_NOT_FOUND("적립된 포인트 정보를 찾을 수 없습니다 PointId : "),

    //Redis
    POINT_POLICY_REDIS_SETTING_FAIL("레디스 setting 에 실패하였습니다."),
}