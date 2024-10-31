package com.sinsa.sinsa_payments.common.exception.enum

enum class ExceptionCode(
    val message: String
) {
    //SUCCESS
    SUCCESS("성공"),

    //Point_Policy
    POINT_POLICY_INVALID_ACCUMULATED("유효하지 않은 적립금액이 저장 요청되었습니다 point : "),
    POINT_POLICY_INVALID_HELD("유효하지 않은 보유가능한 금액이 저장 요청되었습니다 point : "),
    POINT_POLICY_INVALID_DAY_OF_EXPIRED_DATE("유효하지 않은 만료일 저장 요청되었습니다 days : "),

    //Redis
    POINT_POLICY_REDIS_SETTING_FAIL("레디스 setting 에 실패하였습니다."),
}