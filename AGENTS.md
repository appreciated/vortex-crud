# AGENTS Instructions

What Would Have Actually Helped
The Real Issue:

I approached this like a typical Spring Boot project because there was nothing screaming "DON'T DO THAT" when I started looking at the security module.
What Would Have Saved Me Hours:
1. A Big Warning in the Security Module README

# security/README.md

⚠️ DO NOT CREATE CUSTOM REPOSITORIES OR SERVICES ⚠️

This is vortex-crud, not vanilla Spring Boot.
See FormRouteFactory for the correct data access pattern.

2. Working Example

An examples/security-example showing registration + login would have been worth 1000 words. I would have seen:

    No UserRepository methods
    No UserService
    Direct VortexCrudDataStore usage

3. Comment in SecurityConfig Template

4. The README Architecture Section

It explains the architecture well, but it doesn't explicitly say:

    "If you find yourself creating a custom repository method or a @Service class for data access, you're doing it wrong."

Why I Got It Wrong:

    Saw UserRepository exists → thought "I need to add methods to it"
    Saw Spring Security needed → thought "I need a UserDetailsService class"
    Didn't read FormRouteFactory until you told me to
    The existing code doesn't have examples of this pattern in the security context

Bottom Line:

The framework works great, but the "pit of failure" for Spring Boot developers is wide open. A few guardrails in the documentation (especially in the security module) would have prevented me from going down the wrong path three times.

The architecture is actually quite elegant once I understood it - but I had to learn by doing it wrong first!
nfigurations in examples/jpa-sqlite-example and examples/jooq-sqlite-example for the new feature. If you modified an existing feature, please update the existing example.

5. When done with a task, if you find it helpful for your next task, make some adjustments to the AGENTS.md