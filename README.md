# Zaruc - Backend

## Relatório de desenvolvimento <br>
1 - Estudo sobre SpringBoot (iniciando do 0). <br>
2 - Desenvolvimento de um CRUD sem Auth para entender os mapeamentos. <br>
3 - Estudo sobre SpringSecurity. <br>
4 - Adição do SprintgSecutiry com ajuda do tutorial da Fernanda Kippler (https://www.youtube.com/watch?v=5w-YCcOjPD0&t=147s). <br>
5 - Aplicação com Dockerfile e docker-compose. <br>
6 - Resolução de problemas com dependência e erros de rota. <br>
<br>
 
## Documentação
### Rotas:

``` sh
'/auth/register'
{
  login: string,
  name: string,
  password: string,
  role: UserRole (admin or user)
}
```
``` sh
'/auth/login'
{
  login: string,
  password: string,
}
```
``` sh
'/api/users' - GET
[
  {
    id: string,
    login: string,
    name: string,
  },
  ...
]
```
``` sh
'/api/users/{id}' - GET
{
  id: string,
  login: string,
  name: string,
}
```
``` sh
'/api/users/{id}' - PUT
{
  login: string,
  name: string,
}
```
``` sh
'/api/users/{id}' - DELETE
```

