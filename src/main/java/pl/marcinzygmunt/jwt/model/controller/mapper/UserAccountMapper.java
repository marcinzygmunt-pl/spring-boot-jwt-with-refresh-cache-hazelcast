package pl.marcinzygmunt.jwt.model.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import pl.marcinzygmunt.jwt.model.UserRole;
import pl.marcinzygmunt.jwt.model.controller.dto.SignupRequest;
import pl.marcinzygmunt.jwt.model.entity.UserAccountEntity;

import java.util.HashSet;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR, uses = PasswordEncoderMapper.class)
public interface UserAccountMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", source = "email")
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "password", source = "password", qualifiedBy = EncodedMapping.class)
    @Mapping(target = "roles", source = "roles", qualifiedByName = "toUserRole")
    UserAccountEntity toUserAccountEntity(SignupRequest signupRequest);


   @Named("toUserRole")
    default Set<UserRole> toUserRole(Set<String> source) {
        Set<UserRole> roles = new HashSet<>();
        for (String role : source) {
            UserRole userRole = UserRole.valueOf(role);
            roles.add(userRole);
        }
        return roles;
    }

}
