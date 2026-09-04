# Ponytail Audit

## Findings (biggest cut first)

1. `delete:` 4 dead service classes (OrderService, DeliveryService, AuthenticationService, RestaurantService). Main.java uses repositories directly. Nothing replaces them. [src/service/*.java — 97 lines]

2. `delete:` Admin and RestaurantOwner model classes. Never instantiated; Main.java only checks Customer and DeliveryPerson via instanceof. Nothing replaces them. [src/model/Admin.java, src/model/RestaurantOwner.java — 24 lines]

3. `delete:` interfaces/UserRepository.java. Orphaned duplicate of repository/UserRepository.java with different methods (findByEmail vs login), zero implementations, zero imports. Nothing replaces it. [src/interfaces/UserRepository.java — 8 lines]

4. `delete:` PaymentService. Single-method pass-through: `return payment.processPayment()`. Inline the call. Nothing replaces it. [src/service/PaymentService.java — 8 lines]

5. `yagni:` 3 repository interfaces with 1 implementation each (OrderRepository, DeliveryRepository, UserRepository). Inline into Jdbc* classes until a second impl exists. [src/repository/*Repository.java — 25 lines]

6. `stdlib:` DatabaseConfig static block loading JDBC driver via Class.forName. JDBC 4.0+ (Java 6) auto-loads drivers via SPI. Delete the block. [src/config/DatabaseConfig.java:L12-18 — 7 lines]

net: -169 lines, -5 files possible.
