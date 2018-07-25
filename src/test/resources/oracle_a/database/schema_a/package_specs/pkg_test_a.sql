/*
 * Copyright 2018 Kostyantyn Krakovych
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

create or replace package pkg_test_a as

   c_string constant varchar2(100 char) := 'Text with & (ampersand)';

   procedure prc_test_a;

   procedure prc_test_b;

   function fnc_test_a
      return number;

   function fnc_test_b
      return varchar2;

end pkg_test_a;
/
