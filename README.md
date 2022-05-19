# hibernate-starter

sql script is located in the root project dir: script.sql

@PrePersist - moved before save @PreUpdate - moved before update

@Transactional public Long create(UserCreateDto userDto) { // validation var validatorFactory =
Validation.buildDefaultValidatorFactory(); var validator = validatorFactory.getValidator(); var validationResult =
validator.validate(userDto); if (!validationResult.isEmpty()) { throw new ConstraintViolationException(validationResult)
; } var userEntity = userCreateMapper.mapFrom(userDto); return userRepository.save(userEntity).getId(); }

MAPstruct - mapper
