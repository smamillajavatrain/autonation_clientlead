package codinpad.springboot.specification;

import codinpad.springboot.entity.CustomerLead;
import codinpad.springboot.enums.LeadStatus;
import org.springframework.data.jpa.domain.Specification;

public final class CustomerLeadSpecification {

    private CustomerLeadSpecification() {
    }

    public static Specification<CustomerLead> nameContains(String name) {
        return (root, query, cb) ->
                name == null || name.isBlank()
                        ? null
                        : cb.like(
                            cb.lower(root.get("name")),
                            "%" + name.trim().toLowerCase() + "%");
    }

    public static Specification<CustomerLead> emailContains(String email) {
        return (root, query, cb) ->
                email == null || email.isBlank()
                        ? null
                        : cb.like(
                            cb.lower(root.get("email")),
                            "%" + email.trim().toLowerCase() + "%");
    }

    public static Specification<CustomerLead> phoneContains(String phone) {
        return (root, query, cb) ->
                phone == null || phone.isBlank()
                        ? null
                        : cb.like(
                            root.get("phone"),
                            "%" + phone.trim() + "%");
    }

    public static Specification<CustomerLead> managerEquals(Long managerId) {
        return (root, query, cb) ->
                managerId == null
                        ? null
                        : cb.equal(
                            root.get("accountManager").get("id"),
                            managerId);
    }

    public static Specification<CustomerLead> statusEquals(LeadStatus status) {
        return (root, query, cb) ->
                status == null
                        ? null
                        : cb.equal(root.get("status"), status);
    }
}
