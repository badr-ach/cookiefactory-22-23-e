# Cookiefactory-22-23-Team-#template
_Template for classroom SI4-COO_

## doc
Contient le rapport final

## .github
   1. Contient sous workflows/maven.yml, une version d'un fichier d'actions qui est déclenché dès que vous poussez du code. 
Sur cette version initiale seule un test Junit5 est déclenché pour vérifier que tout fonctionne.
       - Github Actions (See in .github/workflows) to simply make a maven+test compilation
  2. Contient sous ISSUE_TEMPLATE, les modèles pour les issues user_story et bug. Vous pouvez le compléter à votre guise.

## src
 - pom.xml : 
       - Cucumber 7 et JUnit 5
       - Maven compatible
       - JDK 17


## User stories 
La liste des fonctionnalités livrées par user story.<br>

### US: Place an Order WITHOUT store choice WITHOUT time choice
**As a** Customer **I want to** order 1 basic recipe **in order** to buy cookies<br>
:star2: **Priorité/Priority :** Must have
:star2: **Estimation/Estimate :** M
**Acceptance criterias:**<br>

_** Background: **_
```
  Given a newly created order
  And a customer named "Bob"
  And a cookie named "Chocolalala" priced 10.0
```

_**Scénario: A new cookie is added to the order**_<br>
```
    When the customer adds the cookie
    Then a new order item is added to the order
    Then the order item quantity is 1
```
_**Scénario: An existing cookie is added to the order**_<br>
```
    Given the order already contains the cookie to be added
    When the customer adds the same cookie
    Then a new order item is not added to the order
    Then the order item quantity is 2
```
_**Scénario: The customer verifies his cart**_<br>
```
    Given the order contains two cookies
    When the customer verifies his cart
    Then the calculated price is equal to 20.00
```

### US: Order validation WITHOUT scheduling WITHOUT store choice
**As a** Customer **I want to** order 1 basic recipe **in order** to buy cookies<br>
:star2: **Priorité/Priority :** Must have
:star2: **Estimation/Estimate :** S

**Acceptance criterias:**<br>
```
- The total is correct (without taxes)
- If the payment was successful the order is payed if not the order stays pending (transaction simulation)
- The receipt is returned
```

_**Background:**_<br>
```
GIVEN a Cookie named Chocololala price 5.0 $
AND a Customer named Bob
AND a Cook name Joe
AND a payment service
AND an order
```

_**Scénario:**_<br>
```
WHEN the customer adds 2 “Chocolalala” cookies priced 5.0 $ to his order	
THEN the cart total is 10.0 $
```

_**Scénario:**_<br>
```
WHEN the customer proceeds to pay the order with a correct credit card number "123456789"
THEN the customer receives the receipt
THEN the order status is PAYED
THEN the order is assigned to a Cook
```

_**Scénario:**_<br>
```
WHEN the customer pays the order with a wrong credit card number "0000000000"
THEN the customer receives an error
THEN the order status is PENDING
```

### US: 
**As a** placeholder **I want to** placeholder **in order** placeholder<br>
:star2: **Priorité/Priority :** 
:star2: **Estimation/Estimate :** 

**Acceptance criterias:**<br>
```
- 
- 
- 
```

_**Background:**_<br>
```
GIVEN 
AND 
AND 
```

_**Scénario:**_<br>
```
WHEN 
THEN 
```


### US: 
**As a** placeholder **I want to** placeholder **in order** placeholder<br>
:star2: **Priorité/Priority :** 
:star2: **Estimation/Estimate :** 

**Acceptance criterias:**<br>
```
- 
- 
- 
```

_**Background:**_<br>
```
GIVEN 
AND 
AND 
```

_**Scénario:**_<br>
```
WHEN 
THEN 
```


### Milestone X

Chaque user story doit être décrite par 
   - son identifiant en tant que issue github (#), 
   - sa forme classique (As a… I want to… In order to…) (pour faciliter la lecture)
   - Le nom du fichier feature Cucumber et le nom des scénarios qui servent de tests d’acceptation pour la story.
   Les contenus détaillés sont dans l'issue elle-même.
   
   
