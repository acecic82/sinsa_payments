# API

### Back-Office

---
#### POST
(1회 최대 적립금 정책을 셋팅하는 API)\
input : 
```kotlin
val maxAccumulatedPoint: Long
```
output : 
```kotlin
data class PointPolicyDTO (
    val maxAccumulatedPoint: BigDecimal,
    val maxHeldPoint: BigDecimal,
    val dayOfExpiredDate: Long
)
```
/api/v1/admin/max-accumulated-point

---
#### POST
(최대 보유 적립금 정책을 셋팅하는 API)\
input : 
```kotlin
val maxHeldPoint: Long
```
output :
```kotlin
data class PointPolicyDTO (
    val maxAccumulatedPoint: BigDecimal,
    val maxHeldPoint: BigDecimal,
    val dayOfExpiredDate: Long
)
```
/api/v1/admin/max-held-point

---
#### POST
(만료일 정책을 셋팅하는 API)\
input :
```kotlin
val days: Long
```
output :
```kotlin
data class PointPolicyDTO (
    val maxAccumulatedPoint: BigDecimal,
    val maxHeldPoint: BigDecimal,
    val dayOfExpiredDate: Long
)
```
/api/v1/admin/day-of-expired-date

---

### Free-Point


---

#### POST
(포인트 적립을 하는 API)\
input :
```kotlin
data class FreePointDTO(
    val memberId: Long,
    val point: BigDecimal,
    val manual: Boolean
)
```
output :
```kotlin
data class FreePointDTO(
    val memberId: Long,
    val point: BigDecimal,
    val manual: Boolean
)
```
/api/v1/free-point/save

---
#### POST
(포인트 적립을 취소하는 API)\
input :
```kotlin
val pointId: Long
```
output :
```kotlin
data class FreePointDTO(
    val memberId: Long,
    val point: BigDecimal,
    val manual: Boolean
)
```
/api/v1/free-point/cancel

---
#### POST
(포인트를 사용하는 API)\
input :
```kotlin
data class FreePointTransactionDTO (
    val memberId: Long,
    val point: String,
    val orderId: String
)
```
output :
```kotlin
Boolean
```
/api/v1/free-point/use

---
#### POST
(포인트 사용을 취소하는 API)\
input :
```kotlin
data class FreePointTransactionDTO (
    val memberId: Long,
    val point: String,
    val orderId: String
)
```
output :
```kotlin
Boolean
```
/api/v1/free-point/use-cancel